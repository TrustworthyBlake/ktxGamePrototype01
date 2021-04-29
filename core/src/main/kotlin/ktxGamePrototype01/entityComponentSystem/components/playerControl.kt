package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

class playerControl {
    var viewport: Viewport
    var stage: Stage
    var isPressed = false

    var cam: OrthographicCamera


    fun draw() {
        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    constructor(batch: SpriteBatch) {
        cam = OrthographicCamera()
        viewport = FitViewport(800f, 480f, cam)
        stage = Stage(viewport, batch)
        stage.addListener(object : InputListener() {
            override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                when (keycode) {
                    Input.Keys.UP -> isPressed = true
                }
                return true
            }

            override fun keyUp(event: InputEvent, keycode: Int): Boolean {
                when (keycode) {
                    Input.Keys.UP -> isPressed = false
                }
                return true
            }
        })
        Gdx.input.inputProcessor = stage
        val table = Table()
        table.left().bottom()
        val upImg = Image(Texture(Gdx.files.internal("graphics/red_square.png")))
        upImg.setSize(50f, 50f)
        upImg.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                isPressed = true
                return true
            }

            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                isPressed = false
            }
        })


        table.add()
        table.add(upImg).size(upImg.width, upImg.height)
        table.add()
        table.row().pad(5f, 5f, 5f, 5f)
        stage.addActor(table)
    }
}