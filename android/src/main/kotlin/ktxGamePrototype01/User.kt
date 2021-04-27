package ktxGamePrototype01

object User {

    private var userID = ""
    private var userName = ""
    private var userEmail = ""
    private var userScore = -1
    private var isTeacher = false
    private var userLoaded = false
<<<<<<< HEAD
    private var courseList: List<String> = emptyList()
    private var teacherList: List<String> = emptyList()
    private var quizList: List<String> = emptyList()

    fun setUser(id: String, name: String, email: String, score: Int, teacher: Boolean, courses: List<String>) {
=======
    private var courseList : List<String> = emptyList()
    private var achievements : List<String> = emptyList()

    fun setUser(id: String, name: String, email: String, score: Int, teacher: Boolean, courses : List<String>, achievList : List<String>) {
>>>>>>> fb3d0f9406b6a0d4539b639ee5ff1e6ab89547d3
        userID = id
        userName = name
        userEmail = email
        userScore = score
        isTeacher = teacher
        if (userID != "") {
            userLoaded = true
        }
        courseList = courses
<<<<<<< HEAD
=======
        achievements = achievList
>>>>>>> fb3d0f9406b6a0d4539b639ee5ff1e6ab89547d3
    }

    fun logoutUser() {
        userID = ""
        userName = ""
        userEmail = ""
        userScore = -1
        isTeacher = false
        userLoaded = false
    }

    fun setName(name: String) {
        userName = name
    }

    fun setEmail(email: String) {
        userName = email
    }

    fun setScore(score: Int) {
        userScore = score
    }

    fun setTeachers(list: List<String>){
        teacherList = list
    }

    fun setQuizes(list: List<String>){
        quizList = list
    }

    fun getQuizes(): List<String> {
        return quizList
    }

    fun getTeachers(): List<String> {
        return teacherList
    }

    fun getName(): String {
        return userName
    }

    fun getEmail(): String {
        return userEmail
    }

    fun getScore(): Int {
        return userScore
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

<<<<<<< HEAD
=======
    fun getAchievement(): List<String>{
        return achievements
    }

>>>>>>> fb3d0f9406b6a0d4539b639ee5ff1e6ab89547d3
}