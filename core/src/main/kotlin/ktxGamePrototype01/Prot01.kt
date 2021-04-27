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
import ktxGamePrototype01.screen.FirstScreen
import ktxGamePrototype01.screen.OpenWorldScreen
import ktxGamePrototype01.screen.SecondScreen

private val LOG = logger<Prot01>()
const val unitScale = 1 / 16f

class Prot01(private val x: Int) : KtxGame<KtxScreen>() {
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
        addScreen(FirstScreen(this))
        //addScreen(SecondScreen(this))
        //setScreen<FirstScreen>()
        addScreen(OpenWorldScreen(this))
        setScreen<OpenWorldScreen>()
        if(x == 2){
            LOG.debug {" is 2" }
        }
    }

    override fun dispose() {
        super.dispose()
        LOG.debug { "Number of sprites in current batch: ${(batch as SpriteBatch).maxSpritesInBatch}" }
        batch.dispose()
        batchText.dispose()
    }
}