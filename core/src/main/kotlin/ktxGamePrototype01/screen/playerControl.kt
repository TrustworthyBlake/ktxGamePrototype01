package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.log.debug
import ktx.log.logger

private val LOG = logger<AbstractScreen>()

class playerControl {
    var viewport: Viewport
    var stage: Stage
    var isPressed = false
    private val atlas = TextureAtlas(Gdx.files.internal("uiskin.atlas"))
    private val skin = Skin(Gdx.files.internal("uiskin.json"), atlas)

    fun draw() {
        stage.act()
        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    constructor(batch: SpriteBatch) {
        val cam = OrthographicCamera()
        viewport = FitViewport(1080f, 1920f, cam)
        stage = Stage(viewport, batch)
        Gdx.input.inputProcessor = stage
        val table = Table()
        val tableText = Table()
        val strLabel = Label("Activate", skin)
        strLabel.setFontScale(4f)
        val activateButton = Image(Texture(Gdx.files.internal("graphics/Button01.png")))
        activateButton.setSize(380f, 120f)
        activateButton.addListener(object : InputListener() {
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
        tableText.setFillParent(true)
        tableText.add(strLabel)
        tableText.bottom().padBottom(30f)
        tableText.touchable = Touchable.disabled

        table.setFillParent(true)
        table.add(activateButton).size(activateButton.width, activateButton.height)
        table.bottom().padBottom(20f)

        stage.addActor(table)
        stage.addActor(tableText)
    }
}