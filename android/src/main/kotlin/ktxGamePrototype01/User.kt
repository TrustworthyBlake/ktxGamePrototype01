package ktxGamePrototype01

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

object User {

    private var userID = ""
    private var userName = ""
    private var userEmail = ""
    private var userScore = -1
    private var isTeacher = false
    private var userLoaded = false
    private var playerHead = "head1"
    private var playerBody = "body1"
    private var courseList: List<String> = emptyList()
    private var teacherList: List<String> = emptyList()
    private var teacherAvatarList = mutableListOf<String>()
    private var quizList: List<String> = emptyList()
    private var quizTeacherList: List<String> = emptyList()
    private var achievements: List<String> = emptyList()

        fun setUser(id: String, name: String, email: String, score: Int, teacher: Boolean, head: String, body: String, courses: List<String>, achievList: List<String>) {
            userID = id
            userName = name
            userEmail = email
            userScore = score
            isTeacher = teacher
            if (userID != "") {
                userLoaded = true
            }
            playerHead = head
            playerBody = body
            courseList = courses
            achievements = achievList
        }

        fun logoutUser() {
            userID = ""
            userName = ""
            userEmail = ""
            userScore = -1
            isTeacher = false
            userLoaded = false
        }

        fun setHeadAndBody(head: String, body: String) {
            playerHead = head
            playerBody = body
        }

        fun setHead(head : String){
            playerHead = head
        }

        fun setBody(body : String){
            playerBody = body
        }

        fun getHeadAndBody(): List<String> {
             return listOf(userName, playerHead, playerBody)
        }

        fun setName(name: String) {
            userName = name
        }

        fun getName(): String {
            return userName
        }

        fun setEmail(email: String) {
            userName = email
        }

        fun getEmail(): String {
            return userEmail
        }

        fun setScore(score: Int) {
            userScore = score
        }

        fun getScore(): Int {
            return userScore
        }

        fun setTeachers(list: List<String>) {
            teacherList = list
        }

        fun getTeachers(): List<String> {
            return teacherList
        }

        fun setQuizes(list: List<String>) {
            quizList = list
        }

        fun getQuizes(): List<String> {
            return quizList
        }

        fun setTeacherForQuizzes(list: List<String>){
            quizTeacherList = list
        }

         fun getTeacherForQuizzes(): List<String> {
            return quizTeacherList
         }

        // Sets the teachers avatar configuration in a list, takes the teacher's name,
        // head and body as String
        fun setTeacherAvatars(name: String, head: String, body: String) {
            // Data tags used by the game engine to sort the types
            val nameDataTag = "name-"
            val headDataTag = "head-"
            val bodyDataTag = "body-"
            val completeTag = "complete-complete"

            // Adds the teacher data to a list if not already existing in list
            if(!teacherAvatarList.contains(nameDataTag+name)){
                teacherAvatarList.add(nameDataTag+name)
                teacherAvatarList.add(headDataTag+head)
                teacherAvatarList.add(bodyDataTag+body)
                teacherAvatarList.add(completeTag)
            }
        }

        fun getTeacherAvatars(): List<String> {
            return teacherAvatarList
        }

        fun checkForTeacher(): Boolean {
            return isTeacher
        }

        fun isUSerLoaded(): Boolean {
            return userLoaded
        }

        fun getCourses(): List<String> {
            return courseList
        }

        fun getAchievement(): List<String> {
            return achievements
        }


        fun getId(): String {
        return userID
        }

        fun addToUserScore() {
            if (Gdx.app.getPreferences("playerData" + userName) != null) {
                val prefs: Preferences = Gdx.app.getPreferences("playerData" + userName)
                var score: Float = prefs.getFloat("totalPlayerScore")
                val scoreInt = score.toInt()
                if (scoreInt != 0) {
                    setScore(scoreInt)
                    updateFirestoreUser()
                }
            }
        }

        fun updateFirestoreUser() {
            DBObject.updateUser(userID, userName, userEmail, userScore, playerHead, playerBody)
        }

}