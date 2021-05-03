package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.log.debug
import ktx.log.logger
import java.awt.Shape
import java.time.format.TextStyle

private val LOG = logger<AbstractScreen>()

class QuizInfo {
    var viewport: Viewport
    var stage: Stage
    var isUpPressed = false
    val vpW = 480f
    val vpH = 800f

    val shapeRenderer = ShapeRenderer()
    val atlas = TextureAtlas(Gdx.files.internal("uiskin.atlas"))
    val skin = Skin(Gdx.files.internal("uiskin.json"), atlas)


    var backgroundHeightModifier = 0f



    fun resize(width: Int, height: Int) {
        viewport.update(width, height, )
    }

    constructor(batch: SpriteBatch, list: MutableList<String>) {
        val cam = OrthographicCamera()
        viewport = FitViewport(vpW, vpH, cam)
        stage = Stage(viewport, batch)
        backgroundHeightModifier = list.size.toFloat()

        // Elements for list
        val exitButton = TextButton("Exit", skin)


        // Keyboard listeners for debugging
        stage.addListener(object : InputListener() {
            override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                return true
            }

            override fun keyUp(event: InputEvent, keycode: Int): Boolean {
                when (keycode) {
                    Input.Keys.UP -> isUpPressed = false
                }
                return true
            }
        })
        Gdx.input.inputProcessor = stage
        val table = Table()
        table.setFillParent(true)

        //  Add touch event when clicking button
        exitButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                isUpPressed = true
                LOG.debug { "button pressed" }
                Gdx.app.exit()
                return true
            }

            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                isUpPressed = false
                Gdx.app.exit()

            }
        })





        table.add()
        table.row()
        for (result in list){
            val textField = Label(result, skin)
            table.add(textField)
            table.row()
        }

        table.add(exitButton)
        table.add()

        stage.addActor(table)
    }




    fun draw() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.setColor(Color.SALMON)
        shapeRenderer.rect((Gdx.graphics.width - (9f * 48))/2, (Gdx.graphics.height - (16f * 48f)+16*backgroundHeightModifier)/2, (9f * 48), (16f * 48f)+16*backgroundHeightModifier)
        shapeRenderer.end()
        stage.act()
        stage.draw()
    }
}