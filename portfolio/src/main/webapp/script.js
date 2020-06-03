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
  const request = document.getElementById('request-input');
  const sorting = document.getElementById('select-sorting');
  fetch('/data?request=' + request.value + '&sorting=' + sorting.value).then(response => response.json()).then(data => {
    const commentList = document.getElementById('comments-container');
    commentList.innerText = "";
    let comments = data.comments;
    for (let i = 0; i < comments.length; ++i) {
        commentList.appendChild(createCommentDiv(comments[i]));
    }
  });
  renderCommentForm();
}

/*
 * Overloaded delete function, deletes specific comment
 */ 
function deleteData(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete-data', {method: 'POST', body: params}).then(response => loadData());
}

/*
 * Creates singular list element given string
 */
function createCommentDiv(comment) {
  let commentDiv = document.createElement('div');
  commentDiv.id = comment.id;

  let senderElem = document.createElement('p');
  senderElem.innerText = comment.sender + " commented:";
  commentDiv.appendChild(senderElem);

  let commentElem = document.createElement('p');
  commentElem.innerText = comment.text;
  commentDiv.appendChild(commentElem);
  
  if (comment.hasOwnProperty('imgURL')) {
    let commentImg = document.createElement('img');
    commentImg.src = comment.imgURL;
    commentDiv.append(commentImg);
  }

  let commentTime = document.createElement('p');
  commentTime.innerText = getTimeDif(comment.timestamp);
  commentDiv.appendChild(commentTime);

  let deleteBtn = document.createElement('button');
  deleteBtn.innerText = "Delete";
  deleteBtn.addEventListener('click', () => {
    deleteData(comment);
    commentDiv.remove();
  })
  commentDiv.appendChild(deleteBtn);

  return commentDiv;
}

/*
 * Returns string representation of time difference between comment and user
 */

function getTimeDif(commentTime) {
  let date = new Date();
  let timeDiffMillis = date.getTime() - commentTime;
  let timeDiff = timeDiffMillis / 1000;
  if (timeDiff < 60) {
    return Math.floor(timeDiff) + " seconds";
  } else if (timeDiff < 3600) {
    return Math.floor(timeDiff/60) + " minutes ago";
  } else if (timeDiff < 86400) {
    return Math.floor(timeDiff/3600) + " hours ago";
  } else {
    return Math.floor(timeDiff/86400) + " days ago";
  }
}

// fetches login status
function getLoginStatus() {
  return fetch('/login-status').then(response => response.json());
}

// renders comment-form based on user login status
function renderCommentForm() {
  getLoginStatus().then(status => {
    const commentForm = document.getElementById('comment-form');
    const logLink = document.getElementById('login-logout-link');

    const isLoggedIn = status.status;
    const url = status.url;

    if (isLoggedIn) {
      commentForm.style.display = 'block';
      logLink.innerText = 'logout here';
    } else {
      commentForm.style.display = 'none';
      logLink.innerText = 'login here';
    }
    logLink.href = url;
  });
}