package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import ktx.ashley.*
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.offsetPos
import ktxGamePrototype01.screen.QuizScreen

private val LOG = logger<InteractableSystem>()
const val WrongAnswerPoints = 0
const val hitboxScalerMax = 2.0f
const val hitboxScalerMin = 0.5f

// Handles collision detection and relevant logic
class InteractableSystem() : IteratingSystem(allOf(InteractableComponent::class, TransformComponent::class).get()) {

    private val playerHitbox = Rectangle()
    private val interactableHitbox = Rectangle()
    private val playerActivationHitbox = Rectangle()

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
    private var numOfBoundEntities = 0


    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        if (interactables.isEmpty()) {
            interactables.add(1)
            LOG.debug { "Spawned" }
        }
    }

    //  Function that edits entities on map and updates user information when player has answered a question
    private fun hasAnsweredQuiz(interact : InteractableComponent, p: PlayerComponent, player: Entity, playerTransform: TransformComponent, correct: Boolean) {

        if(correct) {
            p.playerScore += interact.maxPointsQuestion
            player[QuizComponent.mapper]?.let { quiz ->
                quiz.quizResultList.add(interact.maxPointsQuestion.toString())
            }
        }else{
            p.playerScore += WrongAnswerPoints
            player[QuizComponent.mapper]?.let { quiz ->
                quiz.quizResultList.add("0")
            }
        }
        // RESET START
        playerTransform.posVec3.x = 10.5f - offsetPos
        playerTransform.posVec3.y = 14f
        //  Removes all answer entities
        interactableEntities.forEach { interactable ->
            if (!interact.isQuest && !interact.isTeacher){
               val temp = interactable[InteractableComponent.mapper]
                if (temp != null) {
                    if (temp.isQuestOrAnswer) engine.removeEntity(interactable)
                }
            }
        }
        // Removes all text elements (Question/Answers)
        textEntities.forEach { text ->
            val t = text[TextComponent.mapper]
            require(t != null)
            if (t.isQuizAnswer) engine.removeEntity(text)
        }
        // Sett boolean that allows the quiz system to continue printing the next question
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
            if(interact.type == InteractableType.QUEST)engine.removeEntity(interactable)
        }
    }

    private fun interactWithTeacher(entity: Entity){
        val qQuestComp = entity[QuizQuestComponent.mapper]//quizQuestEntities[QuizQuestComponent.mapper]
        require(qQuestComp != null) { "Error: Missing quiz quest component" }
        removeQuestEntities()
        qQuestComp.showAvailableQuizes = true
    }

    private fun interactWithQuest(p: PlayerComponent, interact: InteractableComponent){
        p.gameInst.addScreen(QuizScreen(p.gameInst, interact.nameOfQuiz, p.playerName))
        if (p.gameInst.containsScreen<QuizScreen>()) {
            LOG.debug { "Switching to FirstScreen" }
            p.gameInst.setScreen<QuizScreen>()
        }
    }



    override fun processEntity(entity: Entity, deltaTime: Float) {

        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity |entity| must have TransformComponent. entity=$entity" }
        val interact = entity[InteractableComponent.mapper]
        require(interact != null) { "Entity |entity| must have TransformComponent. entity=$entity" }
        interactableHitbox.set(
                transform.posVec3.x + 0.25f,
                transform.posVec3.y - 0.0f,
                transform.sizeVec2.x  * hitboxScalerMin,
                transform.sizeVec2.y  * hitboxScalerMin
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
                playerActivationHitbox.set(
                        playerTransform.posVec3.x - 0.5f,
                        playerTransform.posVec3.y - 0.5f,
                        playerTransform.sizeVec2.x* hitboxScalerMax,
                        playerTransform.sizeVec2.y* hitboxScalerMax)



                if(p.playerControl.isPressed) {

                    if (numOfBoundEntities <= 1) {
                        removeBindEntitiesComp(entity)
                        //numOfBoundEntities -= 1


                    }
                    LOG.debug { "Numofboundents = $numOfBoundEntities" }
                }



                // If playerActivationHitbox overlaps with interactable hitbox
                if(playerActivationHitbox.overlaps(interactableHitbox)){
                    if(p.playerControl.isPressed) {

                        //  Based on what type of interactable player interact with, launch relevant function.
                        when(interact.type){
                            InteractableType.CORRECTANSWER -> hasAnsweredQuiz(interact, p, player, playerTransform, true)
                            InteractableType.WRONGANSWER -> hasAnsweredQuiz(interact, p, player, playerTransform, false)
                            InteractableType.TEACHER -> interactWithTeacher(entity)
                            InteractableType.QUEST -> interactWithQuest(p, interact)
                            InteractableType.CATEGORY -> {}
                            InteractableType.ITEM -> {
                                addBindEntitiesComp(player, entity)

                            }
                            else -> LOG.debug { "No Collision Type" }
                        }




                    }



                }




                //  IF PLAYER OVERLAPS WITH HITBOX
                if (playerHitbox.overlaps(interactableHitbox)) {

                    //  SET STANDARD COLLISION
                    if (playerTransform.posVec3.x < interactableHitbox.x) playerTransform.posVec3.x = playerTransform.posVec3.x - 0.069f
                    if (playerTransform.posVec3.x > interactableHitbox.x) playerTransform.posVec3.x = playerTransform.posVec3.x + 0.069f
                    if (playerTransform.posVec3.y < interactableHitbox.y) playerTransform.posVec3.y = playerTransform.posVec3.y - 0.069f
                    if (playerTransform.posVec3.y > interactableHitbox.y) playerTransform.posVec3.y = playerTransform.posVec3.y + 0.069f
                }
            }
        }
    }

    private fun addBindEntitiesComp(playerEnt : Entity, interactEnt : Entity){
        if(!interactEnt.contains<BindEntitiesComponent>(BindEntitiesComponent.mapper)) {
            numOfBoundEntities += 1

            interactEnt.addComponent<BindEntitiesComponent>(engine) {
                masterEntity = playerEnt
                posOffset.set(1f, 0.25f)
            }
        }
    }

    private fun removeBindEntitiesComp(interactEnt : Entity) {
        LOG.debug { "Called remove func" }
        if(interactEnt.contains<BindEntitiesComponent>(BindEntitiesComponent.mapper)){
            interactEnt.remove<BindEntitiesComponent>()
            numOfBoundEntities -= 1
            LOG.debug { "removing bind" }
        }
    }
}



