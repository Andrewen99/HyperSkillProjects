package connectfour

/**
 * Класс непосредственно игры
 * Забирает всю инфу для старта и инициализации игры (кол-во игр, кол-во строк и столбцов)
 * И запускает шарманку
 */
class Game(dimensions: IntArray?, gamesCount: Int?, val p1: Player, val p2: Player) {
    /**
     * По дефолту запрашивает у клиента размер таблицы и заполняет пустыми значениями
     */
    private val board: Board =  dimensions?.let { Board(it) } ?: Board(getInputDimensions())

    /**
     * По дефолту запрашивает у клиента кол-во игр
     */
    private val gamesCount: Int = gamesCount ?: askGamesCount()

    constructor(p1: Player, p2: Player) : this(null, null, p1, p2)

    /**
     * Инициализация и запуск [gamesCount] игр
     */
    fun initAndStartMultipleGames() {
        println("${p1.name} VS ${p2.name}")
        println("${board.rowsCount} X ${board.columnsCount} board")
        if (gamesCount == 1) {
            println("Single game")
        } else {
            println("Total $gamesCount games")
        }
        var isFirstPlayer = true;
        repeat(gamesCount) {
            if (gamesCount > 1)
                println("Game #${it + 1}")
            playSingleGame(isFirstPlayer)
            board.clean()
            isFirstPlayer = !isFirstPlayer
        }
        println("Game Over!")
    }

    /**
     * Шарманка
     */
    private fun playSingleGame(isFirstPlayer: Boolean = true) {
        var isFirstPlayerTurn = isFirstPlayer

        board.draw()

        gameLoop@ while (true) {
            val currentPlayer = if (isFirstPlayerTurn) p1 else p2
            val currentDisk = if (isFirstPlayerTurn) p1.disk else p2.disk

            val columnNum = getAndValidateColumnNumInput(currentPlayer.name)
            if (columnNum == Constants.EXIT_GAME_VALUE) {
                println("Game over!")
                break
            }
            val isDiskInsertSuccessful = board.putDisk(currentDisk, columnNum)
            if (isDiskInsertSuccessful) {
                board.draw()
                when (board.checkTurnResult(currentDisk)) {
                    TurnResult.WIN -> {
                        currentPlayer.score += 2
                        println("Player ${currentPlayer.name} won")
                        printScore()
                        break@gameLoop
                    }
                    TurnResult.DRAW -> {
                        p1.score += 1
                        p2.score += 1
                        println("It is a draw")
                        printScore()
                        break@gameLoop
                    }
                    else -> {}
                }
                isFirstPlayerTurn = !isFirstPlayerTurn
            } else {
                println("Column $columnNum is full")
            }
        }
    }

    private fun askGamesCount(): Int  {
        while (true) {
            println("Do you want to play single or multiple games?")
            println("For a single game, input 1 or press Enter\n" +
                    "Input a number of games:")
            val gamesCount = readln()
            when {
                gamesCount.matches(Regex("\\d+")) -> {
                    if (gamesCount.toInt() > 0) {
                        return gamesCount.toInt()
                    } else {
                        println("Invalid input")
                    }
                }
                gamesCount.isEmpty() -> {
                    return 1
                }
                else -> {
                    println("Invalid input")
                }
            }
        }
    }

    private fun getInputDimensions(): IntArray {
        while (true) {
            println(
                "Set the board dimensions (Rows x Columns)\n" +
                        "Press Enter for default (6 x 7)"
            )
            val dimensions = readLine()!!.trim().replace(Regex("[ \t]"), "").lowercase()
            if (dimensions.isEmpty()) {
                return intArrayOf(6, 7)
            } else if (dimensions.matches(Constants.NUMBERS_INPUT_REGEX)) {
                val numbers = dimensions.split("x").map { it.toInt() }
                if (numbers[0] !in 5..9) {
                    println("Board rows should be from 5 to 9")
                } else if (numbers[1] !in 5..9) {
                    println("Board columns should be from 5 to 9")
                } else {
                    return numbers.toIntArray()
                }

            } else {
                println("Invalid input")
            }
        }
    }

    private fun getAndValidateColumnNumInput(currentPlayer: String): Int {
        var columnNum: Int
        val columnsCount = board.discList[0].size
        while (true) {
            try {
                println("${currentPlayer}'s turn:")
                val inputStr = readLine()!!
                if (inputStr == "end") {
                    return Constants.EXIT_GAME_VALUE
                }
                columnNum = inputStr.toInt()
                if (columnNum !in 1..columnsCount)
                    println("The column number is out of range (1 - $columnsCount)")
                else
                    return columnNum
            } catch (e: NumberFormatException) {
                println("Incorrect column number")
            }

        }

    }

    private fun printScore() = println("Score\n" +
            "${p1.name}: ${p1.score} ${p2.name}: ${p2.score}")
}