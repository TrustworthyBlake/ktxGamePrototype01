package ktxGamePrototype01.entityComponentSystem

// Functions that are used multiple times throughout the system is put here
class HelperFunctions {
    // Max length should be 34 with text scaling at 4.0f for entire textViewport
    // Returns triple = chopped string, how many times the string has been chopped and the offset pos
    // needed for centering text to the textViewport
    fun chopString(str: String, maxLength: Int) : Pair<String, Int> {
        val numChars = str.count()
        var newStr = str
        var spacer = 0
        if(numChars > maxLength) {
            for (i in 0..numChars) {
                if (i.rem(maxLength) == 0) {
                    newStr = StringBuilder(newStr).apply { insert(i + spacer, '\n') }.toString()
                    spacer += 1
                }
            }
        }
        if(spacer < 1) spacer = 1
        return Pair(newStr, spacer)
    }
}