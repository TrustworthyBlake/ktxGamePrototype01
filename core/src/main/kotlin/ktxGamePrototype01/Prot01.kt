package ktxGamePrototype01

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.system.RenderSystem2D
import ktxGamePrototype01.screen.FirstScreen
import ktxGamePrototype01.screen.SecondScreen

private val LOG = logger<Prot01>()
const val unitScale = 1 / 16f

class Prot01 : KtxGame<KtxScreen>() {
    val batch : Batch by lazy { SpriteBatch()}
    val engine : Engine by lazy { PooledEngine().apply { addSystem(RenderSystem2D(batch)) }  }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        LOG.debug { "Game instance created" }
        addScreen(FirstScreen(this))
        addScreen(SecondScreen(this))
        setScreen<FirstScreen>()
    }

    override fun dispose() {
        super.dispose()
        LOG.debug { "Number of sprites in current batch: ${(batch as SpriteBatch).maxSpritesInBatch}" }
        batch.dispose()
    }
}