package ktxGamePrototype01

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.system.*
import ktxGamePrototype01.screen.CategorizeScreen
import ktxGamePrototype01.screen.QuizScreen
import ktxGamePrototype01.screen.OpenWorldScreen

private val LOG = logger<Prot01>()
const val unitScale = 1 / 16f
const val offsetPos = 0.80f

// Main class for game engine creation, takes four input values: which game screen to create,
// username of player, name of the locally stored quiz and a list containing teacher data
class Prot01(private val showScreen: String, private val playerName : String,
             private val quizToUse : String, private val teacherDataList : List<String>?) : KtxGame<KtxScreen>() {

    // Aspect ratio 9:16 horizontal
    val gameViewport = FitViewport(9f, 16f)
    // Batch that will contain the sprites to be drawn by the RenderSystem2D
    val batch : Batch by lazy { SpriteBatch()}
    // Batch that will contain the text strings to be drawn by the RenderSystem2DText
    val batchText: Batch by lazy { SpriteBatch() }

    // Add all created systems that is to be used by the game engine here
    val engine : Engine by lazy { PooledEngine().apply {
        addSystem(PlayerInputSystem(gameViewport))
        addSystem(MovementSystem())
        addSystem(InteractableSystem())
        addSystem(RenderSystem2D(batch, gameViewport))
        addSystem(RenderSystemText2D(batchText))
        addSystem(QuizSystem())
        addSystem(BindEntitiesSystem())
        addSystem(QuizQuestSystem())
        addSystem(CategorizeSystem())
    }  }

    // Adds and sets the game screen based on the showScreen string, defaults to OpenWorldScreen
    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        LOG.debug { "Game instance created" }
        when (showScreen) {
            "QuizScreen" -> {
                addScreen(QuizScreen(this, quizToUse, playerName))
                setScreen<QuizScreen>()
            }
            else -> {
                //addScreen(OpenWorldScreen(this, teacherDataList, playerName))
                //setScreen<OpenWorldScreen>()
                addScreen(CategorizeScreen(this, "dong", playerName))
                setScreen<CategorizeScreen>()
            }
        }
    }

    // Clears the batches out of memory
    override fun dispose() {
        super.dispose()
        LOG.debug { "Number of sprites in current batch: ${(batch as SpriteBatch).maxSpritesInBatch}" }
        batch.dispose()
        batchText.dispose()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
    }
}