package com.github.andrbl.ktxgameprototype01.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import ktxGamePrototype01.Prot01

/** Launches the desktop (LWJGL3) application.  */

    fun main() {
        Lwjgl3Application(Prot01(), Lwjgl3ApplicationConfiguration().apply {
            setTitle("gamePrototype01")
            setWindowedMode(9 * 48, 16 * 48)
            setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
        })
    }
