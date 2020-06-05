// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.*; 

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> existingEvents, MeetingRequest request) {
    ArrayList<TimeRange> mandatoryTimeRanges = new ArrayList<TimeRange>();
    ArrayList<TimeRange> optionalTimeRanges = new ArrayList<TimeRange>();
    // if meeting duration is longer than a day, it is not possible to hold meeting
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
        return mandatoryTimeRanges;
    }
    // initialize time range with whole day
    mandatoryTimeRanges.add(TimeRange.WHOLE_DAY);
    optionalTimeRanges.add(TimeRange.WHOLE_DAY);
    Collection<String> MandatoryAttendants = request.getAttendees();
    Collection<String> optionalAttendants = request.getOptionalAttendees();
    for(Event e: existingEvents) {
      boolean hasMandatoryAttendee = false;
      int numOptionalUnavailable = 0;
      for(String attendee: e.getAttendees()) {
        // if one of the attendees of this event is one of the people we're scheduling this meeting for, then find any time conflicts and remove them
        if (!hasMandatoryAttendee && MandatoryAttendants.contains(attendee)) {
            handleEventConflict(mandatoryTimeRanges, e, request);
            hasMandatoryAttendee = true;
        }
        if (optionalAttendants.contains(attendee)) {
            numOptionalUnavailable++;
        }
      }
      //if all optional attendees are attending this event, then mark this time slot as unavailable for any optional attendees
      if (optionalAttendants.size() != 0 && numOptionalUnavailable == optionalAttendants.size()) {
          handleEventConflict(optionalTimeRanges, e, request);
      }
    }

    // See if there are any valid overlaps between the mandatory time ranges and optional time ranges
    ArrayList<TimeRange> overlapTimeRanges = new ArrayList<TimeRange>();
    int mandatoryIndex = 0;
    int optionalIndex = 0;
    while (mandatoryIndex < mandatoryTimeRanges.size() && optionalIndex < optionalTimeRanges.size()) {
        TimeRange curMandatoryRange = mandatoryTimeRanges.get(mandatoryIndex);
        TimeRange curOptionalRange = optionalTimeRanges.get(optionalIndex);
        // if they overlap, need to see if any common timerange can be used
        TimeRange overlap = getOverlap(curMandatoryRange, curOptionalRange);
        if (overlap != null && overlap.duration() >= request.getDuration()) {
            overlapTimeRanges.add(overlap);
        } 
        if (curOptionalRange.end() <= curMandatoryRange.end()) {
            optionalIndex++;
        }
        if (curMandatoryRange.end() <= curOptionalRange.end()) {
            mandatoryIndex++;
        }
    }
    if (MandatoryAttendants.size() != 0 && overlapTimeRanges.size() == 0) {
        return mandatoryTimeRanges;
    }    
    return overlapTimeRanges;
  }

    private TimeRange getOverlap(TimeRange mandatoryRange, TimeRange optionalRange) {
        if (!mandatoryRange.overlaps(optionalRange)) { 
            return null;
        }
        // if the optional range contains the mandatory range, then the entirety of the mandatory range is shared by all
        if (optionalRange.contains(mandatoryRange)) {
            return mandatoryRange;
        }
        // if the mandatory range contains the optional range, the entirety of optional range is shared by all
        if (mandatoryRange.contains(optionalRange)) {
            return optionalRange;
        }
        if (mandatoryRange.start() < optionalRange.start()) {
            return TimeRange.fromStartEnd(optionalRange.start(), mandatoryRange.end(), true);
        } else {
            return TimeRange.fromStartEnd(mandatoryRange.start(), optionalRange.end(), true);
        }
    }

    private void handleEventConflict(ArrayList<TimeRange> timeranges, Event e, MeetingRequest request) {
        for (int i = 0; i < timeranges.size();) {
            TimeRange curRange = timeranges.get(i);
            TimeRange eventRange = e.getWhen();
            if (curRange.overlaps(eventRange)) {
                // case 1: current timerange is within event timerange => no part of timerange works so remove
                if (eventRange.contains(curRange)) {
                    timeranges.remove(i);
                // case 2: event timerange is within current timerange
                } else if (curRange.contains(eventRange)) {
                    int numNewRanges = 0;
                    // searches for valid timeranges before event and after the event and adds them in order into the timeranges list
                    if (eventRange.start() - curRange.start() >= request.getDuration()) {
                        timeranges.add(i + (++numNewRanges), TimeRange.fromStartEnd(curRange.start(), eventRange.start(), false));                    }
                    if (curRange.end() - eventRange.end() >= request.getDuration()) {
                        timeranges.add(i + (++numNewRanges), TimeRange.fromStartEnd(eventRange.end(), curRange.end(), false));
                    }
                    timeranges.remove(i);
                    i += numNewRanges;
                // case 3: slight slight overlap: either event starts before timerange or starts during and continues past timerange
                } else {
                    int start = curRange.start() < eventRange.start() ? curRange.start() : eventRange.end();
                    int end = curRange.end() < eventRange.end() ? eventRange.start() - 1 : curRange.end();
                    if (end - start >= request.getDuration()) {
                        timeranges.add(i + 1, TimeRange.fromStartEnd(start, end, false));
                        timeranges.remove(i);
                        i++;
                    } else {
                        timeranges.remove(i);
                    }
                }
            } else {
                //if no overlap then timerange is safe, keep untouched
                i++;
            }
        }
    }
}
