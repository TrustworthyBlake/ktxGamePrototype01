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
import ktxGamePrototype01.entityComponentSystem.components.playerControl
import ktxGamePrototype01.entityComponentSystem.system.*
import ktxGamePrototype01.screen.FirstScreen
import ktxGamePrototype01.screen.OpenWorldScreen

private val LOG = logger<Prot01>()
const val unitScale = 1 / 16f

class Prot01(private val showScreen: String, private val playerName : String,
             private val quizToUse : String, private val teacherDataList : List<String>?) : KtxGame<KtxScreen>() {
    val gameViewport = FitViewport(9f, 16f)
    val batch : Batch by lazy { SpriteBatch()}
    val batchText: Batch by lazy { SpriteBatch() }

    val engine : Engine by lazy { PooledEngine().apply {
        addSystem(PlayerInputSystem(gameViewport))
        addSystem(MovementSystem())
        addSystem(InteractableSystem())
        addSystem(RenderSystem2D(batch, gameViewport))
        addSystem(RenderSystemText2D(batchText, gameViewport))
        addSystem(QuizSystem())
        addSystem(BindEntitiesSystem())
        addSystem(QuizQuestSystem())
        addSystem(NukePooledSystem())}  }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        LOG.debug { "Game instance created" }
        when (showScreen) {
            "FirstScreen" -> {
                addScreen(FirstScreen(this, quizToUse, playerName))
                setScreen<FirstScreen>()
            }
            else -> {
                addScreen(OpenWorldScreen(this, teacherDataList, playerName))
                setScreen<OpenWorldScreen>()
            }
        }

    }

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