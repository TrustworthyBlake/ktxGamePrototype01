package com.github.andrbl.ktxgameprototype01.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import ktxGamePrototype01.Prot01
import java.util.ArrayList

/** Launches the desktop (LWJGL3) application.  */

private var textList: ArrayList<String> = ArrayList()

    fun main() {
        Lwjgl3Application(Prot01("1", "", "", textList ), Lwjgl3ApplicationConfiguration().apply {
            setTitle("gamePrototype01")
            setWindowedMode(9 * 48, 16 * 48)
            setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
        })
    }
