# Profprog Report Individual Discussion

### Andreas Blakli
<strong>May 2021 </strong>

## Code I consider to be good: createUserEntityFromPlayerData()

Direct link to code: https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/b26b6c9c500a6510d5c8d5bc46f010f9b2c53cf2/core/src/main/kotlin/ktxGamePrototype01/screen/OpenWorldScreen.kt
\ <strong>createUserEntityFromPlayerData() starts on line 64. </strong>

I consider this code to be good because it is well written and offers good readability. Let me explain in a little more detail: The main purpose of this function is to create the player 
entity with the structure from the ECS (Entity Component System). The player entity is the avatar the player plays as in-game and it is composed of two entites: playerEntityBody and 
playerEntityHead. The name of the function describes very well what it exactly is used for.

Short summary of how it works:

Firstly, it takes and finds the playerData+PlayerUserName.xml file in Androids local storage, it does this by using the playerUserName which is passed into the class this function belongs 
in from the Android instance. Then it gets the strings from the xml file with the user configured avatar, before going any further it checks if the values are null in case something is 
wrong with the data. Secondly, it creates the playerEntityBody which is composed of multiple components. Inside the sprite component the player’s body texture is set accordingly to the 
string provided from the xml file. Finally, it creates the playerEntityHead, it is composed and built up in the same way as playerEntityBody with the string being used to set the correct 
head texture. This entity does not have a movement component, but it has instead bindEntitiesComponent which copies the vector position from the master entity (body) plus an offset value. 

This function is good because it offers good readability as to which components are used, how the values inside each component is set. It has good error handling to make it fail in a predefined 
and controlled way which prevents program crashes. The function also allows for scalability if and or when more avatar options are added to the game. In the when{} block new statements can 
easily be added to handle new strings added to the xml file to set new sprite textures for the body and head of the player avatar.  The code has good comments to explain when something important 
is done, it is also made in mind with the object-oriented programming paradigm. 

## Code I consider to be bad is two functions inside the class QuizSystem: processEntity (entity: Entity, deltaTime: Float) and createQuizTextEntities( quizName: String) 

Direct link to code: https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/b26b6c9c500a6510d5c8d5bc46f010f9b2c53cf2/core/src/main/kotlin/ktxGamePrototype01/entityComponentSystem/system/QuizSystem.kt
\ The reason why I am mentioning these to functions is because they are interconnected with each other. While they are not horrible, they both suffer from flawed logic and bad code practice. They were 
initially used as proof of concept and to be pragmatic, other functionality in the application were prioritized. 

The class itself should have some of its private variables removed or moved inside the functions that use them.
\ <strong>processEntity() starts on line 33. </strong>

The if and else if statements on line 37 and 43 inside processEntity() can be simplified, comments describing what these if’s are for should have been added, because as they are now it can be 
difficult for others who have not worked on this part of they system to understand what is going on. The while loop on line 51 should be replaced with an if since processEntity() itself is a loop 
and therefore it does not need another while loop as it is only supposed to be run once.  All these if statements can be rewritten in more optimized way.
processEntity() is among other things used for calling createQuizTextEntities() when certain conditions are met elsewhere in the program (some of the bools are switched accordingly from other systems). 

<strong>createQuizTextEntities() starts on line 91. </strong>

createQuizTextEntities() is used for creating quiz entities and adding them to the engine based on the list of strings returned from the readQuizFromFile() function. 

createQuizTextEntities() suffers from bad code practice with no comments describing what certain logic is used for or why which causes poor readability.  The “i” variable used in the for loop is a 
class variable which is reset inside processEntity() this should be fixed by moving the “i” variable back inside createQuizTextEntities() function. Inside the for loop there lies a break, in general 
I am not a fan of using break, continue (they of course have their places, but it can introduce unpredictable behavior in the program which can be tricky to tack down if logic is changed later down 
the line), so it would have been nice to remove the break. There are to many variables used inside the function, some of these can be removed, with some changes to the core logic of the function 
which would increase the readability of the code and potentially help the performance a minuscule amount. It also suffers from the use of “magic numbers”, for setting the vector position of rendered 
entities on line 141. Magic numbers in the syntax is concidered very bad code practice as it can exponentially make the code much less maintainable and readable over time if the logic gets expanded 
upon. I believe this function can be rewritten in a way which is more performance optimized and with better code practice. 

## Refractored code: createTeacherEntities(teacherList : List<String>?) and setTeacherAvatars(name: String, head: String, body: String)

Direct link to code before refactoring createTeacherEntities(): https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/b26b6c9c500a6510d5c8d5bc46f010f9b2c53cf2/core/src/main/kotlin/ktxGamePrototype01/screen/OpenWorldScreen.kt

<strong>Before refactoring starts on line 139. </strong>

Direct link to code before refactoring setTeacherAvatars(): https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/b26b6c9c500a6510d5c8d5bc46f010f9b2c53cf2/android/src/main/kotlin/ktxGamePrototype01/User.kt

<strong>Before refactoring starts on line 104. </strong>

Direct link to code after refactoring createTeacherEntities(): https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/a7b70077f42def45c4a80f49ce0887f1aa6b8e07/core/src/main/kotlin/ktxGamePrototype01/screen/OpenWorldScreen.kt

<strong>After refactoring starts on line 137. </strong>

Direct link to code after refactoring setTeacherAvatars(): https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/bf6b9fc3f16fbbefd0696d374a48e2bdb4c0f548/android/src/main/kotlin/ktxGamePrototype01/User.kt

<strong>After refactoring starts on line 104. </strong>

createTeacherEntities() was refactored into createTeachers(teacherList : List<String>?) and createTeacherEntity(teacherName : String, head : String, body: String, teacherPos : Vector2) primarily
to increase code readability and maintainability, but it probably helped the performance a miniscule amount as well. 
Previously createTeacherEntities() provided lackluster readability and was unnecessarily complex with its if statements which used the variables pos and count for engaging certain logic. “pos” 
and “count” was counters. “pos” counted the number of list items that had been iterated through in the list and was reset on mod 3 because that is one complete teacher with all its data. Count 
was used for how many times the loop has been iterated through and primarily for the teacherPosArray which contained the vector coordinates for the spawning location of the teacher entity.

The new function createTeachers() now is responsible for taking the data from the list and pass that to the createTeacherEntity() function. The for loop has been replaced with a forEach and at 
the start of each element in the list there is a datatype identifier. Each element’s string is split into two. The strings are respectively put into their variables: “dataTag” and “data” for the 
actual data. A when block with the dataTag as its condition, is then used to set the data to its corresponding variables. Once the complete tag is seen, createTeacherEntity() is called. It has 
error handling which logs if something unexpected happened with the list. If for some reason something is incorrect with the list the program wont crash as error handling is also provided in later
functions i.e. createTeacherEntity().  The new way this has been setup allows for better maintainability as the when block can easily be extended to handle new features, like for example a new 
hat or item the avatar can wear.

createTeacherEntity() was created to help with the readability of the syntax. The magic numbers have been removed or moved into variables. For the teacherEntityHead the TransformComponent is 
initialized with 0 for the Vector3 position and BindEntitiesComponent have been added to control the position of the head. 

setTeacherAvatars() was refactored to work with the new logic in createTeachers(). It now adds the type ID in front of the main string containing the user configured avatar data. It also adds a 
completed tag in the list to signify that a teacher is completed. 

More comments have been added to the refactored functions to help others who potentially will work with the code in the future, understand what the code is for and what it is doing.

## Professionalism in programming

In reflection of the work I did in the bachelor project, I think could have been more professional with my code. One of the biggest challenges I faced, was when I was trying to be pragmatic. 
Deciding when a piece of code is good enough to then move on to the next tasks set in the product backlog. Pragmatism is a double edge sword as towards the end of the development phase I would 
not prioritize adding comments, as I wrote the code to be a working prototype and was planning on refactoring some parts of it later. This was a mistake as once a system was working; I would 
have to move on to the next task and the planned refactor was never done (it was always put on the “backburner”, and I never got time to fix it). Trying to decide what the right thing to do, 
was difficult and obviously I could have done this better.  It has however been a good learning experience as I will now prioritize to write all comments as I implement the core functionality, 
since its too easy to think, it will get done later. Otherwise, I think I did a good job by sticking to the object-oriented programming paradigm. The entity component system in the game engine 
is an object-oriented approach to dealing with “actors” or objects within a “scene” or game world. One system that got a little out of control and was given to much responsibility in the end was 
the InteractableSystem it is primarily for handling collisions between objects, but it currently also has the responsibility to remove entities from the engine. Removal of entities should be moved 
into a new system specifically designed for that functionality. Automated tests would have been nice to have Implemented but was not prioritized and never implemented. This made testing a little 
tedious in general but made little difference to the testing of the game as that needed actual playtesting by a user. I feel I did a good job with testing the code before pushing into the main 
branch and creating new branches when new major functionality was being developed. 