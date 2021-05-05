package ktxGamePrototype01

import android.content.SharedPreferences
import android.widget.Toast
import java.io.File


class NukeLocalFiles {
     fun nukeStoredQuizzes(activity: AppActivity) {
        val pathInternal = activity?.filesDir
        if (pathInternal != null) {
            val pathTextFile = File(pathInternal, "assets/quizFiles")
            if (pathTextFile.exists()) {
                val children = pathTextFile.listFiles()
                if (children != null) {
                    for (i in children.indices) {
                        children[i].delete()
                    }
                    Toast.makeText(activity, "Deleting", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(activity, "Error: Path is null", Toast.LENGTH_SHORT).show()}
        }else {
            Toast.makeText(activity, "Error: Internal path is null", Toast.LENGTH_SHORT).show()}
    }
    fun nukeSharedPrefs(activity: AppActivity){

    }
}