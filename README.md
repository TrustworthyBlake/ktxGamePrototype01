# Beta Curriculum Application

![Preview Image](https://i.imgur.com/g30nXXg.png)

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Tutorial](#Tutorial)
* [Team](#Team)
* [Assets](#Assets)
* [License](#License)



## General info  
The goal of this project is to create an application environment in which teachers can create their own classrooms with modules that consists of multltiple self-made or imported pre-existing games. Students are able to join multiple classrooms in which they can find and launch the specific games placed there by the teachers. Alternatively students are able to launch an open world game screen where 4 ofthe students teachers are placed on the map, interacting with the separate teachers will give students an option to launch specific games directly from the open world.  


## Demo
Working demo release APK can be downloaded here:  [https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/main/android/release/android-release.apk]
Debug version APK can be downloaded here: []
```
Demo credentials:  
email: student9@test.com  
password: 123456  
```

## Technologies
Project is created using:
* Kotlin version: 1.4
* Firebase Auth: 20.0.2
* Firestore: 22.0.2
* GDX: 1.9.13
* Ashley: 1.7.3
* libKTX: 1.9.13-b1

## Setup
* Clone repository and open project in android studio
* Allow gradle files to be syncronised, may take a while.
* Make sure an emulator installed with minimum Android 8.0 (Oreo)
* Select emulator as launch device, then right-click "AndroidLauncher.kr" and click "Run"

## Tutorial
**Teahers:** Press the create classroom to enter a name for the new classroom, when in a classroom they can post announcements freely. They can add new module by pressing the create button and writing name, or import existing by writing the name of an already existing module. When inside the modules fragment, a game can be imported by writing the name of the game, and new games can be created by selecting the type and clicking create. When creating a quiz, the teacher has one input field with check-boxes to designate the input as question or answer and whether its correct or wrong, pressing "add" adds the question/answer to the stack. After adding each question and designated answer, teacher can write the quiz name and press "Create Quiz" to finish the quiz and have it added to the module in the classroom. Teacher can then proceed to add more quizzes if wanted, or press "Done" to go back to the module view.

**Students:** Classrooms can be joined by students by pressing the join button and inputting the name of the class. When the class has been joined it can be clicked to view the announcements from teachers. When student presses the module option in the menu, the list of modules in the classroom is presented. Clicking a module presents the games it contains, then clicking the game launches the game engine and generates the game based on the data the game contains. Additionally Open world can be launched from the home screen where student can walk to the teachers of their classrooms, press the "Activate" button, and then see optional games from the specific classroom which they can then launch by walking to and pressing the "Activate" button again.

## Team
[![Andreas Blakli](https://i.imgur.com/L2S2IFi.png)](https://github.com/TrustworthyBlake)	 |  [![Theo Camille Gascogne](https://avatars.githubusercontent.com/u/56797646?v=4&s=144)](https://github.com/ApparitionCat) | [![Jesper Ulsrud](https://i.imgur.com/WlXvhjP.png)](https://github.com/jesperu7) | [![Vegard Årnes](https://avatars.githubusercontent.com/u/40339509?v=4&s=144)](https://github.com/VitriolicTurtle)
---|---|---|---
[Andreas Blakli  ](https://github.com/TrustworthyBlake) | [Theo Camille Gascogne](https://github.com/ApparitionCat) | [Jesper Ulsrud](https://github.com/jesperu7) | [Vegard Årnes](https://github.com/VitriolicTurtle)

## Assets:
Button: Wenrexa Assets UI, link: https://wenrexa.itch.io/kit-nesia2  
Trees: hernandack Sideview Tree Pack 1, link: https://hernandack.itch.io/sideview-tree-pack-1  
Skill icons: quintino_pixels Skill Icon Set, link: https://quintino-pixels.itch.io/free-pixel-art-skill-icons-pack  
Rocks: Pixel Overload 48x Rock Tile-maps, link: https://pixeloverload.itch.io/48x-rock-tile-maps  
Quiz Completion Sound: 320655__rhodesmas__level-up-01.mp3, link: https://freesound.org/people/shinephoenixstormcrow/sounds/337049/


## License
[BSD 3](https://choosealicense.com/licenses/bsd-3-clause/)
