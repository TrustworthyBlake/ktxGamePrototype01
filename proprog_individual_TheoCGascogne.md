# Individual discussion.
## Code examples.
### The good code example :
https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/main/android/src/main/kotlin/ktxGamePrototype01/fragments/EditProfileFragment.kt
These functions allow the user to choose a customized avatar to be rendered into the game world. 
The avatar itself is composed of two images, a head and a body, and what sets the user’s choice of avatar head and body combination is a pair of spinners which gives the user a choice between four colours. 
The choice is registered via the two spinners by checking what position the chosen item has in the spinner’s array. 
The string of that item is then given to a variable which is used by a when case to determine which colour it should use and write to a sharedprefs file, and from this file the chosen avatar parts can be read. 
This functionality is paired with four functions, a get and set function for the player avatar’s head and body. 
This functionality and its functions are programmed in a straightforward and simple manner which is easy to read and understand what components are at play here and how they are used. 
For example in the get and set functions, the sharedprefs file is given a keyword and a value associated with it, which by reading it shows the developer exactly what the value written to the file is and how to retrieve that value. 
The commenting is adequate for readability and implementation of this feature is isolated to the class only, which makes it easier to delete or refactor for future development. 
The choice of simple spinners is rather pragmatic, since the alternative is a pop-up list which would require an XML file for the pop-up list (a menu XML file), so spinners keep the setup needed minimal. 
A different alternative would be a recyclerview with an adapter, which would need more XML files and have a dependency on external classes but make it a lot easier to add more items to the list in future development but would be overkill for a list containing only four items. 

### The bad code example:
https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/main/core/src/main/kotlin/ktxGamePrototype01/screen/playerControl.kt
For the bad code example there is  the playerControl class, which is responsible for rendering and giving the game world of the bachelor project an action button, which the player may use to interact with other objects in the game world. 
It uses a viewport and two scenes to render the button and a text for the button as well as a Boolean function to register the button being pressed. 
While it gets the job done the are flaws with this class’s code, the more obvious being the lack of comments explaining what the code inside this class does, which creates a problem due to the code’s reliance on GDX’s badlogic library and the black box like functions from that library, such as draw(). 
This makes it rather inconvenient for developers because a reliance on a library’s needs comments explaining what it does or else it forces developers to read the documentation of the library due to the code’s poor readability. 
The naming convention as well as formating does not help either, as “playerControl” might confuse this class to be responsible for the player object’s movement, if not for how the files are sorted into entity, system, and screen while the formatting makes it look like a block of text with variable declaration left scattered. 
There are magic number in use here for how the button scales to the screen, which does lead to inconsistencies between devices as the function its used for will attempt to scale it with the device’s screen size. Having magic numbers determine any aspect of a program should, if not must, be avoided because a new developer does not know what they do or how they affect the program, in this case any change to the game’s resolution leaves the button unaffected and the developer must search through the code for those magic numbers to implement changes to the button. 
This code could use more readability and some refactoring to remove dependence on magic numbers.

## Refactoring.
For refactoring I decided to refactor every code using the sharedpref class. 
This includes the settingFragment class that sets the themes and every fragment which needs to change based on the theme chosen. Links found here:
### -Original-
#### Appactivity: (line 39-49)
https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/main/android/src/main/kotlin/ktxGamePrototype01/AppActivity.kt
#### sharedprefs:
https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/main/android/src/main/kotlin/ktxGamePrototype01/sharedprefs.kt
#### settingsFragment:
https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/main/android/src/main/kotlin/ktxGamePrototype01/fragments/SettingFragment.kt
#### userFragment (Note: the user and teacher fragments have the same changes so im listing only one of them): (line 59-84)
https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/main/android/src/main/kotlin/ktxGamePrototype01/fragments/UserFragment.kt

### -Refactored-
#### Appactivity: (line 43-49)
https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/refactorTheo/android/src/main/kotlin/ktxGamePrototype01/AppActivity.kt
#### sharedprefs:
https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/refactorTheo/android/src/main/kotlin/ktxGamePrototype01/sharedprefs.kt
#### settingFragment:
https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/refactorTheo/android/src/main/kotlin/ktxGamePrototype01/fragments/SettingFragment.kt
#### userFragment: (line 60-63 and line 198-224)
https://github.com/TrustworthyBlake/ktxGamePrototype01/blob/refactorTheo/android/src/main/kotlin/ktxGamePrototype01/fragments/UserFragment.kt
The refactoring was primarily done to optimise the function and keep it to a minimum. 
In the sharedpref class, the set and load functions for each theme were identical to each other which made the usage of those functions poorly optimised in the AppActivity and settingsFragment.  
Nested if else statements were needed to include a check of all four Boolean values inside the sharedpreference file and the settings fragment would have to use several functions to set one bool to true and the rest to false. 
The class was simply cluttered with redundant code and functions which made using the functions of the sharedprefs class introduce that same redundancy in other classes. 
The refactored version uses only two set and load functions for darkmode, which was left unchanged, and for the themes. 
Instead of using a boolean for the theme values and having five of them, a string is set to the keyword instead which can be used by all themes. 
With this simplified version of sharedprefs, the usage of its functions became a lot cleaner and readable, allowing for when cases to load themes and only one set function needed to set any theme to be used, thus removing any nested if else statements and slightly improving performance. 
During refactoring, improved readability was added to the code by adding more comments where I deemed necessary and any code inside onCreate related to themes (and the user’s avatar) were made into functions instead to de-clutter some of the code inside onCreate.

## Reflecting on professional programming.
Looking back at what I had done in the bachelor project, I feel as if my biggest problem and challenge I had to face was to follow any sort of convention. 
While coding I would focus too much on getting something done rather than asking myself if I have followed the proper coding convention my group agreed on which, when neglected, my group members made sure I was reprimanded for it. 
Communication is key when developing a software with a team of colleagues as the code you write is not just for you, they will have to read it one way or another, which is why proper code conventions must be followed to achieve good readability and understandability for your team.
As time went by and our project started looking like a real prototype, I became aware of the need to follow programming paradigms as it would spare my team members, or people reading our GitHub repository any confusion.
I also had some mishaps with the way I code. 
Pragmatism is a good way of coding simple and understandable code, but I felt that getting used to pragmatic coding often lead to overthinking if a snippet of code was pragmatic enough or needed refactoring to be more scalable, and at times I would willingly skip conventions such as commenting with the excuse of “its pragmatic”. 
On the subject of pragmatism, what I found curious is how we conducted testing. I had read about testing in the past semesters prior to participating in this bachelor project. 
I thought tests would be well documented, step-by-step check lists. 
But due to how much our code would change during development, tests were hardly documented and were done manually, which I feel was polar opposite to what I expected. 
In hindsight of this, while my team didn’t need to document the tests we conducted by ourselves, it would have been beneficial to document how we conducted our tests so we would have a proper checklist to catch any underlying bugs which would come back to annoy future development if left unchecked, especially when we ourselves spent more time than we expected developing features using libraries due to a lack of documentation of those libraries when used together with kotlin.
To add to this, the usage of git as workflow made manual tests simpler to conduct without much consequence from errors since we agreed on having two or more team members to supervise merging of branches to quickly patch any bugs that would crash the program. 
In the end, the project was a great learning opportunity and a helpful insight in professional programming. 
