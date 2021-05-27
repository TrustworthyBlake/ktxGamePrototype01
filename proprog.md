# Profprog Report

### List of students in the team

<strong>Group members and roles:  </strong>

* Andreas Blakli - Group Leader  

* Vegard O. Årnes - Group Secretary  

* Theo Camille Gascogne - Group Member  

* Jesper Ulsrud - Group Member  

### List of links to repos connected to this project

First repo for application prototype - https://github.com/VitriolicTurtle/alphaBSc  

Main repo for the project - https://github.com/TrustworthyBlake/ktxGamePrototype01  

## Strengths and Weaknesses of Kotlin

The bachelor project was an Android application with a standard Android front-end and a game engine running separately from the application. Kotlin was the main programming
language for the project and was chosen over Java because it is trying to solve a lot of problems Java inherently has with its somewhat chaotic nature, its unintuitive way
of writing code and the null exception problem. Kotlin is being heavily pushed onto developers by Google and other software companies are switching over. In the second year
we had the course Mobile/Wearable Programming and we learned Kotlin instead of Java for Android development.  

### Strengths

Kotlin is a good programming language which has many strengths. When creating a program in Kotlin the number of written lines of code is less compared to writing the same program
in Java. Kotlin havs a more intuitive syntax than Java which is easier to understand, increases readability and maintainability of the code. Kotlin was designed to be optimized
towards two programming paradigms: object-oriented and functional programming. Asynchronous coroutines is incorporated into Kotlin to allow for easier code writing of asynchronous
tasks. Coroutines seeks to be better and a replacement for RxJava. Kotlin has backwards-compatibility with Java and allows for the use of Java syntax inside the Kotlin code. The
code is checked for errors and bugs at compile time so the developer will be notified if they have written illegal or incorrect syntax. Having the program syntax checked before
runtime is good as some bugs can go more easily undetected or be more difficult to track down when the program is running.  

### Weaknesses

We feel that two of the biggest weaknesses Kotlin has is the lack of official documentation for certain components and it is difficult to get help with certain programmatic problems.
The lack of certain documentation for e.g. Firebase makes progress with the implementation slow as one has to figure out how to convert code examples from Java to Kotlin and write
it in a good way which fits with the current implantation/solution. Since fewer people use Kotlin compared to Java, getting help for specific problems connected to the Kotlin
syntax is difficult and for the most part one have to figure out a solution to the problem for oneself. This makes writing code in Kotlin at times extremely time consuming.  

## Control of Process and Communication

For structuring the team we created two hierarchies the first hierarchy was for general group responsibilities i.e. group leader and secretary. The second was who had the main
responsibility for a given system within the application. Combine these two hierarchies with the agile development method Scrum and you get the complete sandwich we used to
structure ourselves in the project. We had daily sessions with each other every workday 09:00 to 16:00 and weekly 30 minute meetings on Mondays with the stakeholders and study
supervisor. These hierarchies and timetables were created to improve the work conditions for all group members as having a general structure everyone can follow hinders uncertainty
and confusion for everyone involved with the project.  

The previously mentioned structuring methods were used to control the development process: and primarily by following the Scrum methodology. The Scrum sprints, all planned and
completed work related to the Scum process with e.g. product backlog were done through digital means, this also goes for everything else, all work in general related to the
bachelor project. We feel that doing the bachelor project only through digital means in general went well, but it is not a complete replacement for psychical sessions. Because
having psychical sessions allows for more direct communications and feedback as you can talk a lot more with your hands and use gestures. Ideas can quickly be drawn onto a
whiteboard be discussed, scratched and changed. We substituted the whiteboard with Microsoft Whiteboard which is a digitized whiteboard where people can collaborate together.
MS Whiteboard was mediocre to use and it is a lot more tedious to draw shapes and write down ideas in it, as you have draw with the mouse than compared a real psychical whiteboard.
We mainly used Discord for communication between each other and Zoom for communication with stakeholders and the study supervisor. One of the main issues we had when communicating
digitally is that it is a lot easier to talk over each other, which at times can be very annoying and frustrating.  

 Discord has its strength and weaknesses. Some of the main problems we ran into was when we shared the screen with each other, the text would be extremely blurry due to high resolution
 of our monitors and the 720p quality limit for people without the premium service Nitro. We worked around this problem by scaling up the syntax in the IDE or by switching over to
 Zoom where we could stream in high quality. Some of Discord positives were that it worked great for us with its voice connectivity as we had a primary voice channel dedicated to
 project work and development. Discord's text messaging system worked great, important messages could be pinned in the main bachelor text channel so they would not go unnoticed.  

 Zoom was only used to handle commutations with the stakeholders and study supervisor and for the occasional screen share of code syntax when Discord were insufficient. Zoom's messaging
 system is mediocre for a live chat, for sending notes and other important messages we had to use Discord since Zoom leaves a lot to be desired with its messaging system.  

 In reflection of this we feel that we did a good job with the commutation with each other, the stakeholders and study supervisor. We had good work conditions where we encouraged,
 motivated and helped each other throughout the development process.  


### Version Control System

### Programming Style Guide

### Libraries, and Modularisation of Product

### Libraries

Technologies other than Kotlin and their respective versions:  
* libGDX: 1.9.13

* libKTX: 1.9.13-b1

* Ashley: 1.7.3

* Firebase Auth: 20.0.2

* Firestore: 22.0.2

#### LibGDX/LibKTX  
LibGDX/LibKTX are two of the primary libraries used to develop the game aspect of the application, with LibGDX being the base library, and LibKTX being an extension for increased
compatibility between LibGDX and Kotlin. As LibGDX is based on OpenGL ES, games developed in it are inherently cross-platform and will work on Android, iOS, BlackBerry, Linux, Mac OS X,
Windows, and web browser with WebGL support.

#### Ashley  
Ashley is an extension library for LibGDX/LibKTX that provides functionality for building an entity framework. This was therefore a primary component in building the component based
entity-system present in the game engine.

#### Firestore  
Firestore is a flexible and scalable database solution provided by Firebase and Google Cloud. It offers a NoSQL database solution allowing for data to be stored inside documents in
collections, and can be accessed directly from Android, iOS, and web applications through their native SDKs. However, as the LibKTX engine runs separately from the android SDK,
the game cannot dynamically receive data on runtime, but needs to fetch everything before being launched.

#### Firebase Auth  
Firebase Authentication provides authentication services which follows industry standards like OpenID Connect and OAuth 2.0. It is easily integrable with custom backends and could
therefore seamlessly be used as a secure authorisation mechanism for the application. This method of authentication works as a placeholder, as a further developed solution would
rather integrate a public authentication method, for example FEIDE, or university related email addresses.

### Modularisation of Product  
As game engine and application exist independently from each other the modularisation of each have to be summed up separately from each other. When using the individual operating
systems SDK for developing the application side of the service, the modularisation of the product can more easily follow the standards of the specific platform. Therefore, even though
an application UI could be implemented in LibGDX/LibKTX alone, developing with standard SDKs was favoured as they provide more robust functionalities for providing a good user experience.
As a consequence, The application part of the product has to be written for each operating system, while the game just needs to be made launchable from within said operating system.
In this product the application side was exclusively developed for android, as porting it over to for example iOS would inhibit development of new functionality for the software.

#### Android Application  
The modularisation of the application comes in the form of the standards within android. The navigation system and page view are built up of multiple fragments existing independently
from each other, but shares user information from the database which is fetched on login. RecyclerView are used for each list and are in most cases clickable. Upon clicking a clickable
RecyclerView item a specific fragment type is openee, and the ID of the specific item is then passed into the fragment. The ID is then used to fetch more information about the item from
the database, and fragment is thereafter filled with relevant information. By using the RecyclerView in this manner, there can be an indefinite amount of classrooms, modules and games.
This is especially visible when creating a game inside of a module, as the teacher would only need to define what game-type they with to create, which then launches a fragment for filling
the specific game information. When teacher has completed the creation of a new game, the game ID is added to the modules game list, and the content of the game is added to the database.
Thus, RecyclerViews of games were developed with the potential to contain any different game type, and adding new game modes will require minimal modification in the existing logic allowing 
teachers to implement new games to their modules. As such the scalability of the application as wanted.

#### Game Engine  
The game engine is inherently modular as its based on an Entity Component framework. This means that when entities are created, a set of components are defined within the entities, and
continuously running systems will execute in on the specific entities containing the relevant component. An example of this is how entities might contain a movement component, then the
movement system would execute on every entity with said movement component. The component contains default values that can be set when creating the entities, or changed on runtime, e.g.
velocity in movement example. Scaling up the functionalities would therefore mean either defining multiple different types within the systems, so that the system execute differently on
similar components based on variables set in the component, or creating entirely new components and systems.  
Each separate game is developed within the boundary of their own separate LibGDX screen. Therefore all specific entities only meant to be created in the specific games are defined there,
and all specific game logic can be restricted to the specific LibGDX screen that the player is viewing. That way, all functionality is modular and can be included where its wanted in the
individual games.


### Professionalism in Software Development Process  
The software development process followed the main principles of Scrum, though a bit modified for a smaller group. The modification came in the form of having product owner and scrum master
be equally a part of the development team, though had the additional responsibilities. The project was started by product owner creating a defined list of features, i.e. the product backlog,
that needed to be implemented, thereafter all members would develop prototypes for their expectations surrounding the final product. The prototypes were developed simply by drawing and 
explaining the ideas behind the functionalities in group, then concluding on the overall design of the product and developing it as a presentable prototype in Adobe XD. The first finalised
prototype was thereafter presented to clients and their opinions about what was lacking and what should be modified noted down and taken into consideration for the next prototype. Through this prototyping, multiple necessary features were realized, namely the importance of a highly graphical user experience within the games, and clients’ wish for a playable open world type game
which could utilise the teaching curricula content for the individual student playing. After the finalised product backlog had been formed, it was separated into smaller, tasks and put into
a Trello task board to keep every member updated on other members current task. To assist with creating the product backlog, UML diagrams were also pre-emptively created, that includes a
Use Case Diagram, as well as a database class diagram. After completion of pre-development engineering methodology, a project plan was created for us to lay out a plan for the following
months to optimise execution, a GANTT diagram was created there which was intended to be an estimation of sprint productivity throughout the semester. The GANTT diagram was therefore a
more detailed plan of execution of product backlog tasks.  
In order to keep true to the Scrum methodology, individual responsibility was emphasised. As there is no specific active tester role, everyone were held responsible for testing their own and others functionalities, the methodology for which is detailed in chapter 4. Throughout the process we also arranged the specific ceremonies related to Scrum, i.e. sprint planning, sprint review, sprint retrospective, and daily scum meetings. The benefits yielded from the meetings included providing insight for all members, and increased the ability for group members to assist each other,
in addition, through the project were at times overly pragmatic when developing functionality, by not automating or optimising properly, and the review sessions helped identify where refraction was necessary.Finally, one element we believe could have increased the professionalism if it were to be added, is an attempt to estimate cost of production. This is because the finished product
and thesis was intended as an early prototype for clients to consider when potentially starting development of a proper product, meaning it would be valuable info for potential investors.


### Use of Code Review
To help us to perform the code reviews for our Bachelor project, we implemented different ground rules and guidelines that each member of the team had to follow during development. This was primarily to help us increase the quality of our code and find defects and bugs in the system. For the system development model our group decided on using Scrum for the Bachelor Project. We decided to have weekly sprints and daily meetings, so most of the time we were all communicating with each other in discord while developing, and we kept this in the back of our heads to decide what kind of guidelines we would use. We used our experience from previous courses like Cloud Technologies and Mobile Programming to set up our code review guidelines. 

We ended up deciding on using the standard Kotlin coding conventions as we were developing an Android application in Kotlin. These conventions consist of many different guidelines like for example, source file names and organization, class layouts, naming rules and formatting. As it is quite a long list of different conventions and rules to check and make sure you are following at all times, a shortlist with the most important ones was set up for us to follow. These are described more detailed in the "Programming Style" section.

For the weekly scrum meetings we went through the most important tasks from the previous week and we reviewed the other group members' code. Our group leader would decide who reviewed what code. We would check that the code was up to the standards we set in our coding guidelines. If a member reviewed a bit of code that they felt was not good enough they would make a note, and all of the notes would be reviewed by the whole group at the end of the session. This way we could check up after each sprint to assure the code was working and up to the standard we wanted. 
As we had daily meetings with all group members attending every day we had a lot of opportunities for pair programming and continuous reviewing of code if any member needed help or wanted someone else to review their solution for their current task. If there was a more complex task we would sometimes set two people to do pair programming from the start to get a better and faster implemented solution. 

For each sprint we would take notice of mistakes, and see if we could use the mistakes to improve our guidelines further. This worked really well the first couple of sprints, and we were all happy with how the code and the project progressed. The review sessions after every sprint helped us make the code much more readable and maintainable. The bugs and defects the application was having were also found and dealt with swiftly. 

Unfortunately as time passed our priorities shifted a little bit. The scope of the project caught up with us and slowly we slipped on the code reviews. The review meetings after the sprints had to be cut some of the weeks to prioritize finishing all the tasks for the previous sprint. The code quality suffered because of this, and therefore problems could arise when new code were to be implemented around older code that was far from the standard it should, or we wanted it to be. 

We still did a lot of pair programming, which was very helpful for us in solving many tasks more effectively than it would have been, compared to just being handled by a single person. But some tasks still took a fair bit more time than necessary. We definitely should have caught on to this problem sooner, and realized that the lack of quality assurance and code reviews made it more difficult for us. The weekly review meetings should have been prioritized much higher, as it would have made many tasks much easier to implement and many bugs easier to detect and fix. 

For the fact that we were unable to perform any user testing for the application should also have meant that we needed to have been more thorough with our code reviews. Some bugs that could have been easily detected in testing were noticed later than we would have preferred and became an inconvenience to take care of. The main thing we take away from this is the importance of keeping up the quality control and standards set from the start of the project. We had a system that worked great in the beginning, and because our priorities shifted and we got more and more stressed about things needing to be finished the overall quality of the code and project went down. 
