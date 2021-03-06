<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
   String uploadUrl = blobstoreService.createUploadUrl("/upload-handler"); %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Emmie's Portfolio</title>
    <link rel="stylesheet" href="style.css">
    <script src="script.js"></script>
  </head>
  <body onload="loadData()">
    <div id="content">
      <h1>My Portfolio</h1>
      <div id="greeting-container"></div>
        <div>
            <h1>Hi, I'm Emmie</h1>
            <p>Hello! I'm 19 years old and I'm from Boston, MA.</p>
            <img id="avatar" src="./images/me.jpeg" />
        </div>
        <div>
            <p>Click here to get a random fact about me:</p>
            <button onclick="addRandomFact()">FACT</button>
            <div id="fact-container"></div>
        </div>
        <div>
            <p>This is my dog, Salem</p>
            <img src='./images/salem.JPG' alt='Salem' width='500'/> 
        </div>
        <div>
            <h2>About</h2>
            <p>I'm a computer science and mathematical computation double major at University of Massachusetts Amherst. I am currently a STEP Intern at Google and software engineer for <a href='#'>Build UMass</a>.<p>
            <div>
                <h3>Developer</h3>
                <p>Ever since I first taught myself to code in 7th grade, I fell in love with the joy of building software that can reach a vast number of people all over the world. After attending Make School Summer Academy in SF back in 2018, I began making iOS apps and have made over 10 apps, 2 of which are on the app store and 4 of which have won hackathons. More recently, I began delving into web development and have picked up an interest in the more theoretical and algorithmic side of CS.</p>
            </div>
            <div>
                <h3>Hacker</h3>
                <p>Over the past two years I've attended over 10 hackathons, and have spent countless sleepless nights surrounded by amazing hackers working to build something great and impactful. I enjoy the thrill of brainstorming and implementing a solution to a larger issue within the 24-36 hours of the event and the challenge of problem solving and working with new technologies and people each time.</p>
            </div>
            <div>
                <h3>Collaborator</h3>
                <p>Through hackathons, clubs, summer programs, and jobs, I've gotten to know and collaborate with some of the most brilliant people who've inspired me to keep working hard and pursue my goals. With CS being such a diverse industry, I love that I can connect with and learn from people of all different skills and backgrounds. I also enjoy sharing my experiences and helping others grow and have served as a TA for AP Java as well as a programming instructor at <a href='https://thecodewiz.com/westford-ma/'>Code Wiz</a> and <a href='https://www.ivy-seed.com/'>Ivy Seed Academy</a>.</p>
            </div>
        </div>
        <div id="projects">
            <h2>Projects</h2>
            <div id="website" class="project" style="display: block;">
                <img src="./images/project-images/myweb.png" />
                <h3>My Website</h3>
                <p>React, MaterialUI</p>
                <p>An online portfolio built from scratch.</p>
                <a href='https://emaohn.github.io/my-website'><button>Visit Website</button></a>
                <p>1/13</p>
            </div>
            <div id="koso" class="project" style="display: none;">
                <img src="./images/project-images/koso.png" />
                <h3>Koso</h3>
                <p>Swift</p>
                <p>An all-in-one project management app that allows users to breakdown their project into agendas, todo lists, and notes. This was the first app I made and put on the App Store.</p>
                <a href='https://apps.apple.com/us/app/koso/id1423489655'><button>View on App Store</button></a>
                <p>2/13</p>
            </div>
            <div id="fetch" class="project" style="display: none;">
                <img src="./images/project-images/fetch.png" />
                <h3>Fetch</h3>
                <p>Swift</p>
                <p>An app that uses AR to identify and add items in store to a virtual shopping cart that makes shopping experiences for shoppers and employees easier. First place winner at MIT Blueprint 2019.</p>
                <a href='https://devpost.com/software/fetch-5exjfu'><button>View on Devpost</button></a>
                <p>3/13</p>
            </div>
            <div id="shawa" class="project" style="display: none;">
                <img src="./images/project-images/shawa.png" />
                <h3>Shawa</h3>
                <p>Swift</p>
                <p>An app that helps save water by preventing users from taking long showers using music. Winner of best environmental sustainability waste reduction hack at Tech Together Boston 2019.</p>
                <a href='https://devpost.com/software/shawa'><button>View on Devpost</button></a>
                <p>4/13</p>
            </div>
            <div id="cerberus" class="project" style="display: none;">
                <img src="./images/project-images/cerberus.png" />
                <h3>Cerberus</h3>
                <p>Swift / Web / Arduino</p>
                <p>Smart home security and management device that can be controlled and monitored with paired iOS app. Winner of best hardware hack, best UI, and best usage of Cloud at HackWPI.</p>
                <a href='https://youtu.be/uIitPL-Zco4'><button>View on Youtube</button></a>
                <p>5/13</p>
            </div>
            <div id="auxilia" class="project" style="display: none;">
                <img src="./images/project-images/auxilia.png" />
                <h3>Auxilia</h3>
                <p>Swift / Web</p>
                <p>Mulit-platform application crowdsourcing to gather community support and donation money towards non-government funded issues. Second place winner at MAHacks.</p>
                <a href='https://github.com/emaohn'><button>View on Github</button></a>
                <p>6/13</p>
            </div>
            <div id="keepprivate" class="project" style="display: none;">
                <img src="./images/project-images/keepprivate.png" />
                <h3>KeepPrivate</h3>
                <p>Swift</p>
                <p>KeepPrivate keeps users accountable by providing hundreds of tips regarding safe and appropriate behavior on the Internet and Social Media.</p>
                <a href='https://apps.apple.com/us/app/keepprivate/id1437201924'><button>View on App Store</button></a>
                <p>7/13</p>
            </div>
            <div id="simplyreading" class="project" style="display: none;">
                <img src="./images/project-images/simplyreading.png" />
                <h3>SimplyReading</h3>
                <p>Python / React</p>
                <p>SimplyReading makes reading difficult texts much easier by simplifying the language using a custom metric that we created. Made at TreeHacks 2020.</p>
                <a href='https://devpost.com/software/simplyreading'><button>View on Devpost</button></a>
                <p>8/13</p>
            </div>
            <div id="openwhen" class="project" style="display: none;">
                <img src="./images/project-images/openwhen.png" />
                <h3>OpenWhen</h3>
                <p>SwiftUI</p>
                <p>An app simulating "open when..." cards that allows you to send heartwarming messages and be there for your friends even when far away. Made at BostonHacks 2019.</p>
                <a href='https://devpost.com/software/openwhen-h7x9et'><button>View on Devpost</button></a>
                <p>9/13</p>
            </div>
            <div id="swole" class="project" style="display: none;">
                <img src="./images/project-images/swole.png" />
                <h3>SwoleApp</h3>
                <p>Swift, SwiftUI</p>
                <p>An iOS app with a partner Apple watch app that monitors your weight lifting movements helps you maintin good form. Made at HackUMass VI.</p>
                <a href='https://github.com/emaohn/SwoleApp'><button>View on GitHub</button></a>
                <p>10/13</p>
            </div>
            <div id="impact" class="project" style="display: none;">
                <img src="./images/project-images/impact.png" />
                <h3>Impact</h3>
                <p>Swift / Web</p>
                <p>An "action -> consequence" based app that encourage people to make positive impacts on earth by awarding and deducting points based on a variety of data. Made at HackMIT 2019.</p>
                <a href='https://devpost.com/software/impact-pq2mah'><button>View on Devpost</button></a>
                <p>11/13</p>
            </div>
            <div id="messages" class="project" style="display: none;">
                <img src="./images/project-images/messages.png" />
                <h3>Messages</h3>
                <p>Swift</p>
                <p>Messages helps people get out of awkward situations by sending custom fake text notifications at any given time. Made at my first hackathon, HackUMass V.</p>
                <a href='https://github.com/emaohn/SelfMessage'><button>View on Github</button></a>
                <p>12/13</p>
            </div>
            <div id="splitbill" class="project" style="display: none;">
                <img src="./images/project-images/splitbill.png" />
                <h3>SplitBill</h3>
                <p>Swift</p>
                <p>An app that makes splitting bills with your large groups of people easy and intuitive. Made at Make School Summer Academy.</p>
                <a href='https://github.com/emaohn/SplitBill'><button>View onGithub</button></a>
                <p>13/13</p>
            </div>
            <div class="project-nav">
                <button onClick="toggleProject(-1)">Previous</button>
                <button onClick="toggleProject(1)">Next</button>
            </div>
        </div>
        <div>
            <h2>Photography</h2>
            <p>I started photography when I got my first camera back in 6th grade. What started with me taking random pictures of my dog turned into a huge hobby. I started doing freelance photography for many small Instagram businesses and exploring other types of photography: street, landscape, macro, etc. </p>
            <div class="photo-grid">
                <div class="photo-col">
                    <img src="./images/photography/droplets.jpg" />
                    <img src="./images/photography/IMG_2145.jpg" />
                    <img src="./images/photography/flower1.jpg" />
                </div>
                <div class="photo-col">
                    <img src="./images/photography/IMG_2146.jpg" />
                    <img src="./images/photography/IMG_2147.jpg" />
                </div>
                <div class="photo-col">
                    <img src="./images/photography/salem3.jpg" />
                    <img src="./images/photography/squirrel1.jpg" />
                    <img src="./images/photography/wildfire.jpg" />
                </div>
            </div>
            <div>
                <p>You can check out more of my photography on my <a href="https://www.instagram.com/emmieohn/">Instagram</a> or <a href="https://unsplash.com/@emaohn">Unsplash</a>.</p>
            </div>
        </div>
        <div id="comment-section">
            <h2>Comments</h2>
            <label for="request">Choose how many comments you'd like to display:</label>
            <input type="number" name="request" id="request-input" value="10"/>
            <label for="sorting">Sort by:</label>
            <select name="sorting" id="select-sorting" onChange="loadData()">
                <option value="newest">Newest</option>
                <option value="oldest">Oldest</option>
            </select>
            <button onClick="loadData()">Apply</button>
            <div id="comments-container"></div>
            <div id="comment-form-container">
                <form id="comment-form" method="POST" enctype="multipart/form-data" action="<%= uploadUrl %>">
                    <label for="name">Nickname (Optional): </label><br>
                    <input type="text" name="name" placeholder="Johnny Appleseed" /><br>
                    <label for="comment">Comment:</label><br>
                    <input type="text" name="comment" />
                    <input type="file" name="image" />
                    <input type="submit" /><br>
                </form>
                <a id="login-logout-link"></a>
            </div>
        </div>
        <div id="contact">
            <h2>Contact</h2>
            <p>Have questions or want to work on something together? You can find me here:</p>
            <a href="https://github.com/emaohn"><img alt="GitHub" src="./images/media-icons/github.png" /></a>
            <a href="https://linkedin.com/in/emmieohnuki"><img alt="LinkedIn" src="./images/media-icons/linkedin.png" /></a>
            <a href="mailto:emaohn@gmail.com"><img alt="Email" src="./images/media-icons/email.png" /></a>
            <a href="https://facebook.com/emmieohnuki"><img alt="Facebook" src="./images/media-icons/facebook.png" /></a>
            <a href="https://www.instagram.com/emmieohn/"><img alt="Instagram" src="./images/media-icons/instagram.png" /></a>
        </div>
    </div>
  </body>
</html>
