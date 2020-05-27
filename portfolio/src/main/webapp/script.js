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

/**
 * Adds a random greeting to the page.
 */
function addRandomFact() {
  const facts =
      ['I\'m a sophomore CS/Math major at UMass Amherst.', 'I make iOS apps.', 'I make websites.', 'I like hackathons.', 'I like dogs.', 'I like photography.', 'I speak 3-4 languages.', 'I play tennis.'];

  // Pick a random greeting.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}

/*
 * Displays next project to the page.
 */

function toggleProject(direction) {
    const projects = ["website", "koso", "fetch", "shawa", "cerberus", "auxilia", "keepprivate", "simplyreading", "openwhen", "swole", "impact", "messages", "splitbill"];
    for (let i = 0; i < projects.length; ++i) {
        let curProjectDiv = document.getElementById(projects[i]);
        if (curProjectDiv.style.display === "block") {
            curProjectDiv.style.display = "none";
            let nextProjectIndex = i + direction === projects.length ? 0 : (i + direction === -1 ? projects.length - 1 : i + direction);
            let nextProjectDiv = document.getElementById(projects[nextProjectIndex]);
            nextProjectDiv.style.display = "block";
            break;
        }
    }
}

/*
 * Displays data from /data url in DOM
 */

function loadData() {
  fetch('/data').then(response => response.json()).then(data => {
    const commentList = document.getElementById('server-container');
    commentList.innerText = "";
    let comments = data.comments;
    for (let i = 0; i < comments.length; ++i) {
        commentList.appendChild(createListElement(comments[i].text));
    }
  })
}

/*
 * Creates singular list element given string
 */
function createListElement(s) {
  let listElem = document.createElement('li');
  listElem.innerText = s;
  return listElem;
}
