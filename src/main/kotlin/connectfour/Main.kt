package connectfour

import connectfour.Constants.P1_DISK
import connectfour.Constants.P2_DISK

enum class TurnResult {
    WIN,
    DRAW,
    STILL_PLAYING
}

fun main() {
    println("Connect Four")
    println("First player's name:")
    val p1Name = readLine()!!
    println("Second player's name:")
    val p2Name = readLine()!!
    val game = Game(
        p1 = Player(p1Name, P1_DISK),
        p2 = Player(p2Name, P2_DISK)
    ).also { it.initAndStartMultipleGames() }

}


object Constants {
    val NUMBERS_INPUT_REGEX = Regex("\\d+x\\d+")
    const val EXIT_GAME_VALUE = -1
    const val EMPTY_SPACE = " "
    const val P1_DISK = "o"
    const val P2_DISK = "*"
}