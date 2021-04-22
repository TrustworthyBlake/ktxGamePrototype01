package ktxGamePrototype01

object User {

    private var userID = ""
    private var userName = ""
    private var userEmail = ""
    private var userScore = -1
    private var isTeacher = false
    private var userLoaded = false
    private var courseList : List<String> = emptyList()
    private var achievements : List<String> = emptyList()

    fun setUser(id: String, name: String, email: String, score: Int, teacher: Boolean, courses : List<String>, achievList : List<String>) {
        userID = id
        userName = name
        userEmail = email
        userScore = score
        isTeacher = teacher
        if (userID != "") {
            userLoaded = true
        }
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

    fun setName(name: String) {
        userName = name
    }

    fun setEmail(email: String) {
        userName = email
    }

    fun setScore(score: Int) {
        userScore = score
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

    fun getAchievement(): List<String>{
        return achievements
    }

}