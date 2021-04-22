package ktxGamePrototype01.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Batch
import ktx.app.KtxScreen
import ktxGamePrototype01.Prot01

abstract class AbstractScreen(
        val game:Prot01,
        val engine: Engine = game.engine,
        val batch: Batch = game.batch,
) : KtxScreen