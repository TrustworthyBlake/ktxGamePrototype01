package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import ktx.ashley.*
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.screen.QuizScreen

private val LOG = logger<InteractableSystem>()
const val WrongAnswerPoints = 0

// Handles collision detection and relevant logic
class InteractableSystem() : IteratingSystem(allOf(InteractableComponent::class, TransformComponent::class).exclude(NukePooledComponent::class).get()) {

    private val playerHitbox = Rectangle()
    private val interactableHitbox = Rectangle()

    private val playerEntities by lazy {
        engine.getEntitiesFor(allOf(PlayerComponent::class).get())
    }
    private val interactableEntities by lazy {
        engine.getEntitiesFor(allOf(InteractableComponent::class).get())
    }
    private val textEntities by lazy {
        engine.getEntitiesFor(allOf(TextComponent::class).get())
    }
    private val quizEntities by lazy {
        engine.getEntitiesFor(allOf(QuizComponent::class).get())
    }
    private val quizQuestEntities by lazy {
        engine.getEntitiesFor(allOf(QuizQuestComponent::class).get())
    }

    private val interactables = mutableListOf<Int>()
    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        if (interactables.isEmpty()) {
            interactables.add(1)
            LOG.debug { "Spawned" }
        }
    }

    //  Function that edits entities on map
    private fun hasAnsweredQuiz(interact : InteractableComponent) {
        interactableEntities.forEach { interactable ->
            if (!interact.isQuest && !interact.isTeacher){
            engine.removeEntity(interactable)
            }
        }
        textEntities.forEach { text ->
            val t = text[TextComponent.mapper]
            require(t != null)
            if (t.isQuizAnswer) engine.removeEntity(text)
        }
        quizEntities.forEach { quiz ->
            val q = quiz[QuizComponent.mapper]
            require(q != null)
            q.playerHasAnswered = true
        }
    }

    // Removes the quest entities
    private fun removeQuestEntities(){
        interactableEntities.forEach { interactable ->
            val interact = interactable[InteractableComponent.mapper]
            require(interact != null)
            // If it is a quest entity its removed
            if(interact.isQuest)engine.removeEntity(interactable)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity |entity| must have TransformComponent. entity=$entity" }
        val interact = entity[InteractableComponent.mapper]
        require(interact != null) { "Entity |entity| must have TransformComponent. entity=$entity" }
        interactableHitbox.set(
                transform.posVec3.x,
                transform.posVec3.y,
                transform.sizeVec2.x,
                transform.sizeVec2.y
        )
        playerEntities.forEach { player ->
            val p = player[PlayerComponent.mapper]
            require(p != null)
            player[TransformComponent.mapper]?.let { playerTransform ->
                playerHitbox.set(
                        playerTransform.posVec3.x,
                        playerTransform.posVec3.y,
                        playerTransform.sizeVec2.x,
                        playerTransform.sizeVec2.y
                )
                //  IF PLAYER OVERLAPS WITH HITBOX
                if (playerHitbox.overlaps(interactableHitbox)) {
                    if(p.playerControl.isPressed) {

                        //  IF CORRECT ANSWER IS SELECTED, CONTINUE QUIZ
                        if (interact.correctAnswer) {
                            //  COUNT SCORE
                            p.playerScore += interact.maxPointsQuestion
                            player[QuizComponent.mapper]?.let { quiz ->
                                quiz.quizResultList.add(interact.maxPointsQuestion.toString())
                            }

                            // RESET START
                            playerTransform.posVec3.x = 4.5f
                            playerTransform.posVec3.y = 10f
                            //  Run update on entities
                            hasAnsweredQuiz(interact)
                        } else if (!interact.isQuest && !interact.isTeacher) {
                            p.playerScore += WrongAnswerPoints
                            player[QuizComponent.mapper]?.let { quiz ->
                                quiz.quizResultList.add("0")
                            }
                            // RESET START
                            playerTransform.posVec3.x = 4.5f
                            playerTransform.posVec3.y = 10f
                            //  Run update on entities
                            hasAnsweredQuiz(interact)
                        }
                        // If it is teacher entity the QuizQuestSystem will create the quest entities based on showAvailableQuizes bool
                        if (interact.isTeacher) {
                            val qQuestComp = entity[QuizQuestComponent.mapper]//quizQuestEntities[QuizQuestComponent.mapper]
                            require(qQuestComp != null) { "Error: Missing quiz quest component" }
                            removeQuestEntities()
                            qQuestComp.showAvailableQuizes = true
                        }
                        // Starts the quiz game
                        if (interact.isQuest) {
                            p.gameInst.addScreen(QuizScreen(p.gameInst, interact.nameOfQuiz, p.playerName))
                            if (p.gameInst.containsScreen<QuizScreen>()) {
                                LOG.debug { "Switching to FirstScreen" }
                                p.gameInst.setScreen<QuizScreen>()
                            }
                        }
                    }
                    //  SET STANDARD COLLISION
                    if (playerTransform.posVec3.x < interactableHitbox.x) playerTransform.posVec3.x = playerTransform.posVec3.x - 0.07f
                    if (playerTransform.posVec3.x > interactableHitbox.x) playerTransform.posVec3.x = playerTransform.posVec3.x + 0.07f
                    if (playerTransform.posVec3.y < interactableHitbox.y) playerTransform.posVec3.y = playerTransform.posVec3.y - 0.07f
                    if (playerTransform.posVec3.y > interactableHitbox.y) playerTransform.posVec3.y = playerTransform.posVec3.y + 0.07f
                }
            }
        }
    }
}



