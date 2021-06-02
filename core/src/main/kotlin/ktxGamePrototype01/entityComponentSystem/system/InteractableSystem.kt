package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import ktx.ashley.*
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.offsetPos
import ktxGamePrototype01.screen.CategorizeScreen
import ktxGamePrototype01.screen.QuizScreen

private val LOG = logger<InteractableSystem>()
const val WrongAnswerPoints = 0
const val hitboxScalerMax = 2.0f
const val hitboxScalerMin = 0.5f

// Handles collision detection and relevant logic
class InteractableSystem() : IteratingSystem(allOf(InteractableComponent::class, TransformComponent::class).get()) {

    private val playerHitbox = Rectangle()
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
    private val categorizeEntities by lazy {
        engine.getEntitiesFor(allOf(CategorizeComponent::class).get())
    }

    private val interactables = mutableListOf<Int>()
    private var numOfBoundEntities = 0
    private var timeCounter = 0f
    private var categorizeGameEnd = false
    private var isCategorizeGameMode = false

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        if (interactables.isEmpty()) {
            interactables.add(1)
            LOG.debug { "Spawned" }
        }
        timeCounter += deltaTime
        if (timeCounter > 1000f) timeCounter = 0f
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
            if(interact.type == InteractableType.QUEST_QUIZ)engine.removeEntity(interactable)
        }
    }

    private fun interactWithTeacher(entity: Entity){
        val qQuestComp = entity[QuizQuestComponent.mapper]//quizQuestEntities[QuizQuestComponent.mapper]
        require(qQuestComp != null) { "Error: Missing quiz quest component" }
        removeQuestEntities()
        qQuestComp.showAvailableQuizes = true
    }

    private fun interactWithQuestQuiz(p: PlayerComponent, interact: InteractableComponent){
        p.gameInst.addScreen(QuizScreen(p.gameInst, interact.nameOfGame, p.playerName))
        if (p.gameInst.containsScreen<QuizScreen>()) {
            LOG.debug { "Switching to FirstScreen" }
            p.gameInst.setScreen<QuizScreen>()
        }
    }

    private fun interactWithQuestCategorize(p: PlayerComponent, interact: InteractableComponent){
        p.gameInst.addScreen(CategorizeScreen(p.gameInst, interact.nameOfGame, p.playerName))
        if (p.gameInst.containsScreen<CategorizeScreen>()) {
            LOG.debug { "Switching to FirstScreen" }
            p.gameInst.setScreen<CategorizeScreen>()
        }
    }



    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity |entity| must have TransformComponent. entity=$entity" }
        val interact = entity[InteractableComponent.mapper]
        require(interact != null) { "Entity |entity| must have TransformComponent. entity=$entity" }

        when(interact.type){
            InteractableType.CATEGORY -> {
                interact.interactableHitbox.set( // Todo make bigger
                transform.posVec3.x + 0.25f,
                transform.posVec3.y,
                transform.sizeVec2.x * hitboxScalerMin,
                transform.sizeVec2.y * hitboxScalerMin
                )
            }
            else -> {
                interact.interactableHitbox.set(
                transform.posVec3.x + 0.25f,
                transform.posVec3.y - 0.0f,
                transform.sizeVec2.x  * hitboxScalerMin,
                transform.sizeVec2.y  * hitboxScalerMin
                )
            }
        }

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

                if(player[CategorizeComponent.mapper] != null) isCategorizeGameMode = true

                if(p.playerControl.isPressed && !categorizeGameEnd && isCategorizeGameMode) {
                    removeBindEntitiesComp(entity, timeCounter, p)
                }

                // If playerActivationHitbox overlaps with interactable hitbox
                if(playerActivationHitbox.overlaps(interact.interactableHitbox)){
                    if(p.playerControl.isPressed) {

                        //  Based on what type of interactable player interact with, launch relevant function.
                        when(interact.type){
                            InteractableType.CORRECTANSWER -> hasAnsweredQuiz(interact, p, player, playerTransform, true)
                            InteractableType.WRONGANSWER -> hasAnsweredQuiz(interact, p, player, playerTransform, false)
                            InteractableType.TEACHER -> interactWithTeacher(entity)
                            InteractableType.QUEST_QUIZ -> interactWithQuestQuiz(p, interact)
                            InteractableType.QUEST_CATEGORIZE -> interactWithQuestCategorize(p, interact)
                            InteractableType.CATEGORY -> {}
                            InteractableType.ITEM -> addBindEntitiesComp(player, entity, timeCounter)
                            else -> LOG.debug { "No Collision Type" }
                        }
                    }
                }

                //  IF PLAYER OVERLAPS WITH HITBOX
                if (playerHitbox.overlaps(interact.interactableHitbox)) {

                    //  SET STANDARD COLLISION
                    if (playerTransform.posVec3.x < interact.interactableHitbox.x) playerTransform.posVec3.x = playerTransform.posVec3.x - 0.069f
                    if (playerTransform.posVec3.x > interact.interactableHitbox.x) playerTransform.posVec3.x = playerTransform.posVec3.x + 0.069f
                    if (playerTransform.posVec3.y < interact.interactableHitbox.y) playerTransform.posVec3.y = playerTransform.posVec3.y - 0.069f
                    if (playerTransform.posVec3.y > interact.interactableHitbox.y) playerTransform.posVec3.y = playerTransform.posVec3.y + 0.069f
                }
            }
        }
    }

    private fun addBindEntitiesComp(playerEnt : Entity, interactEnt : Entity, timeCount: Float){
        if(!interactEnt.contains<BindEntitiesComponent>(BindEntitiesComponent.mapper)
                && numOfBoundEntities < 1 && timeCounter > 1.0f) {  // < less than
            numOfBoundEntities += 1

            interactEnt.addComponent<BindEntitiesComponent>(engine) {
                masterEntity = playerEnt
                posOffset.set(1.0f, 0.25f)
            }
            timeCounter = 0f
        }
    }

    private fun removeBindEntitiesComp(interactEnt : Entity, timeCount: Float, p: PlayerComponent) {
        if(interactEnt.contains<BindEntitiesComponent>(BindEntitiesComponent.mapper)
                && numOfBoundEntities >= 1 && timeCounter > 0.5f) { // < less than
            interactEnt.remove<BindEntitiesComponent>()
            numOfBoundEntities -= 1
            timeCounter = 0f
            val x = categorizedItemCheck(interactEnt, p)
            if (x) {
                LOG.debug { "Categorize Completed" }
                categorizeEntities.forEach { category ->
                    val catComp = category[CategorizeComponent.mapper]
                    require(catComp != null)
                    catComp.categorizeResultList.add(p.playerScore.toString())
                    catComp.categorizeIsCompleted = true
                    catComp.showResultList = true
                }
                categorizeGameEnd = true
                /*interactableEntities.forEach {
                    comp -> comp.removeAll()
                }*/
            }
        }
    }

    private var numOfItemsInCorrectCategory = 0
    private var numOfItemsInWrongCategory = 0
    private var itemsList = mutableListOf<Entity>()
    private var itemTypeIsCorrectList = mutableListOf<Boolean>()

    private fun categorizedItemCheck(interactEnt : Entity, p: PlayerComponent) : Boolean { // This is retarded
                                                                     // Todo find a better solution

        var sumOfCategorizedItems = 0
        var maxPoints = 0
        var pointSplitOnNumOfItems = 0
        var categorizeGameCompleted = false
        var check = false

        val interact = interactEnt[InteractableComponent.mapper]
        require(interact != null)

        //Todo fix numOfItems correct and wrong, must be decremented when moved outside hitbox
        if(interact.type == InteractableType.ITEM) {
            LOG.debug { "Inside if for type: category" }
            interactableEntities.forEach { interactable2 ->
                val interact2 = interactable2[InteractableComponent.mapper]
                require(interact2 != null)
                if (interact2.type == InteractableType.CATEGORY && interact.interactableHitbox.overlaps(interact2.interactableHitbox)){
                    maxPoints = interact2.maxPointsQuestion
                    if (interact.belongsToCategory == interact2.belongsToCategory) {
                        numOfItemsInCorrectCategory += 1
                        itemsList.add(interactEnt)
                        itemTypeIsCorrectList.add(true)
                        check = true
                        //itemTypeNrList.add(0, Pair("numOfItemsInCorrectCategory", numOfItemsInCorrectCategory))
                        LOG.debug { "numOfItemsInCorrectCategory= $numOfItemsInCorrectCategory" }
                    }else{
                        numOfItemsInWrongCategory += 1
                        itemsList.add(interactEnt)
                        itemTypeIsCorrectList.add(false)
                        check = true
                        //itemTypeNrList.add(1, Pair("numOfItemsInWrongCategory", numOfItemsInWrongCategory))
                        LOG.debug { "numOfItemsInWrongCategory= $numOfItemsInWrongCategory" }
                    }
                }else if(itemsList.contains(interactEnt) && !check){
                    val index = itemsList.indexOf(interactEnt)
                    if (itemTypeIsCorrectList.elementAt(index)) numOfItemsInCorrectCategory -= 1
                    else numOfItemsInWrongCategory -= 1
                    itemsList.remove(interactEnt)
                    itemTypeIsCorrectList.removeAt(index)
                }
            }
            LOG.debug { "numOfItemsInCorrectCategory= $numOfItemsInCorrectCategory" }
            LOG.debug { "numOfItemsInWrongCategory= $numOfItemsInWrongCategory" }
        }


        val (numOfCategories, numOfItems) = categoriesAndItemsCount()
        sumOfCategorizedItems = numOfItemsInCorrectCategory + numOfItemsInWrongCategory

        if(sumOfCategorizedItems == numOfItems) {
            categorizeGameCompleted = true
            pointSplitOnNumOfItems = maxPoints / numOfItems
            p.playerScore += pointSplitOnNumOfItems * numOfItemsInCorrectCategory
            LOG.debug { "Score= ${pointSplitOnNumOfItems * numOfItemsInCorrectCategory}" }
        }
        return categorizeGameCompleted
    }

    private fun categoriesAndItemsCount() : Pair<Int, Int> {
        var numOfCategories = 0
        var numOfItems = 0
        interactableEntities.forEach { interactable ->
            val interact = interactable[InteractableComponent.mapper]
            require(interact != null)
            when(interact.type){
                InteractableType.CATEGORY -> numOfCategories += 1
                InteractableType.ITEM -> numOfItems += 1
            }
        }
        return Pair(numOfCategories, numOfItems)
    }
}