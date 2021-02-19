package ktxGamePrototype01

import android.content.Context
import android.content.SharedPreferences


class sharedprefs(context: AndroidLauncher) {



    //--------------------------old code
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("sharedPrefDark", Context.MODE_PRIVATE)



    //save Night Mode state as TRUE or FALSE

    fun setDarkModeState(state: Boolean?){
        val editor = sharedPreferences.edit()
        editor.putBoolean("Dark", state!!)
        editor.apply()
    }

    //load Night Mode
    fun loadDarkModeState(): Boolean? {
        val state : Boolean = sharedPreferences.getBoolean("Dark", false)
        return (state)
    }

    fun setRedModeState(state: Boolean?){
        val editor = sharedPreferences.edit()
        editor.putBoolean("Red", state!!)
        editor.apply()
    }

    fun loadRedModeState(): Boolean? {
        val state : Boolean = sharedPreferences.getBoolean("Red", false)
        return (state)
    }

    fun setOrangeModeState(state: Boolean?){
        val editor = sharedPreferences.edit()
        editor.putBoolean("Orange", state!!)
        editor.apply()
    }

    fun loadOrangeModeState(): Boolean? {
        val state : Boolean = sharedPreferences.getBoolean("Orange", false)
        return (state)
    }

    fun setGreenModeState(state: Boolean?){
        val editor = sharedPreferences.edit()
        editor.putBoolean("Green", state!!)
        editor.apply()
    }

    fun loadGreenModeState(): Boolean? {
        val state : Boolean = sharedPreferences.getBoolean("Green", false)
        return (state)
    }

    fun setPurpleModeState(state: Boolean?){
        val editor = sharedPreferences.edit()
        editor.putBoolean("Purple", state!!)
        editor.apply()
    }

    fun loadPurpleModeState(): Boolean? {
        val state : Boolean = sharedPreferences.getBoolean("Purple", false)
        return (state)
    }

}