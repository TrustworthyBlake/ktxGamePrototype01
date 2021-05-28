package ktxGamePrototype01

import android.content.Context
import android.content.SharedPreferences


class sharedprefs(context: AppActivity) {



    //this variable holds the SharedPreference files "sharedPrefDark"
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("sharedPrefDark", Context.MODE_PRIVATE)

    //save Night Mode state as TRUE or FALSE
    fun setDarkModeState(state: Boolean?){
        //open the file to edit, given to variable 'editor'
        val editor = sharedPreferences.edit()
        //puts a boolean value with the keyword "Dark"
        editor.putBoolean("Dark", state!!)
        //apply or save the change
        editor.apply()
    }

    //load Night Mode
    fun loadDarkModeState(): Boolean? {
        val state : Boolean = sharedPreferences.getBoolean("Dark", false)
        return (state)
    }

    fun setThemeColour(theme: String){
        val editor = sharedPreferences.edit()
        editor.putString("Colour", theme!!)
        editor.apply()
    }

    fun loadThemeColour(): String? {
        val chTheme : String? = sharedPreferences.getString("Colour", "Default")
        return (chTheme)
    }

}