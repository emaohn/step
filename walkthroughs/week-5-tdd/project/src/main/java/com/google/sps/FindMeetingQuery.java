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
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> timeranges = new ArrayList<TimeRange>();
    // if meeting duration is longer than a day, it is not possible to hold meeting
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
        return timeranges;
    }
    // initialize time range with whole day
    timeranges.add(TimeRange.WHOLE_DAY);
    Collection<String> meetingAttendants = request.getAttendees();
    for(Event e: events) {
      for(String attendee: e.getAttendees()) {
        // if one of the attendees of this event is one of the people we're scheduling this meeting for, then find any time conflicts and remove them
        if (meetingAttendants.contains(attendee)) {
            modifyTimeRanges(timeranges, e, request);
        }
      }
    }
    // See if my optional attendees can make any of these time slots

    return timeranges;
  }

    private void modifyTimeRanges(ArrayList<TimeRange> timeranges, Event e, MeetingRequest request) {
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
            // if timerange is after event, don't need to look at this or the rest since they're in order
            } else if (curRange.start() > eventRange.end()) {
                break;
            } else {
                //if no overlap then timerange is safe, keep untouched
                i++;
            }
        }
    }
}
