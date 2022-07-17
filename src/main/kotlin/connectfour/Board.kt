package connectfour

/**
 * Класс таблицы
 */
class Board(dimensions: IntArray) {
    val rowsCount: Int = dimensions[0]
    val columnsCount: Int = dimensions[1]
    val discList: MutableList<MutableList<String>> = MutableList(rowsCount) {
        MutableList(columnsCount) { Constants.EMPTY_SPACE }
    }


    fun draw() {
        val rowsNum = discList.size
        val columnsNum = discList[0].size
        //1 2 3 4 5 6 7
        repeat(columnsNum) {
            print(" ${it + 1}")
        }
        println()
        repeat(rowsNum) { i ->
            repeat(columnsNum) { j ->
                print(if (j == 0) "║${discList[discList.lastIndex - i][j]}║" else "${discList[discList.lastIndex - i][j]}║")
            }
            println()
        }
        repeat(columnsNum) {
            when (it) {
                0 -> {
                    print("╚═╩")
                }
                columnsNum - 1 -> {
                    println("═╝")
                }
                else -> {
                    print("═╩")
                }
            }
        }
    }

    /**
     * В этот метод всегда попадает валидный столбец
     * @return true - если успешно поставили диск
     *          false - если колонка уже переполнена
     */
    fun putDisk(disk: String, column: Int): Boolean {
        val actualColumn =
            column - 1 // все же у нас отсчет с нуля а не единицы, поэтому пользовательский столбец смещается на -1
        for (i in 0..discList.lastIndex) {
            if (discList[i][actualColumn] == Constants.EMPTY_SPACE) {
                discList[i][actualColumn] = disk
                return true
            }
        }
        return false
    }

    fun checkTurnResult(disk: String): TurnResult {
        var isBoardFull = true
        discList.forEachIndexed() { rowIndex, row ->
            row.forEachIndexed() { diskIndex, currentDisk ->
                if (currentDisk == disk) {
                    if (checkHorizontalCondition(intArrayOf(rowIndex, diskIndex), disk) ||
                        checkVerticalCondition(intArrayOf(rowIndex, diskIndex), disk) ||
                        checkDiagonalCondition(intArrayOf(rowIndex, diskIndex), disk)
                    )
                        return TurnResult.WIN
                } else if (currentDisk == Constants.EMPTY_SPACE) {
                    isBoardFull = false
                }
            }
        }
        return if (isBoardFull)
            TurnResult.DRAW
        else
            TurnResult.STILL_PLAYING
    }

    /**
     * Проверяем, что правее элемента есть еще минимум 3 столбца для выполнения условий победы
     * Если остальные 3 столбца имеют тот же диск, то победа!
     */
    private fun checkHorizontalCondition(position: IntArray, disk: String): Boolean {
        val x = position[0]
        val y = position[1]
        if (discList[x].lastIndex - y >= 3) {
            for (i in 1..3) {
                if (discList[position[0]][y + i] != disk) {
                    return false
                }
            }
            return true
        } else
            return false
    }

    /**
     * Проверяем, что выше элемента есть еще минимум 3 строки для выполнения условий победы
     * Если остальные 3 столбца имеют тот же диск, то победа!
     */
    private fun checkVerticalCondition(position: IntArray, disk: String): Boolean {
        val x = position[0]
        val y = position[1]
        if (discList.lastIndex - x >= 3) {
            for (i in 1..3) {
                if (discList[x + i][y] != disk) {
                    return false
                }
            }
            return true
        } else
            return false
    }

    /**
     * Проверяем правую диагональ наверх и вниз
     *    * и *
     *   *     *
     *  *        *
     * *           *
     */
    private fun checkDiagonalCondition(position: IntArray, disk: String): Boolean {
        val x = position[0]
        val y = position[1]
        // правая диагональ наверх
        if (discList.lastIndex - x >= 3 && discList[x].lastIndex - y >= 3) {
            for (i in 1..3) {
                if (discList[x + i][y + i] != disk) {
                    return false
                }
            }
            return true
        }
        // правая диагональ вниз
        if (discList.lastIndex - y >= 3 && x >= 3 ) {
            for (i in 1..3) {
                if (discList[x - i][y + i] != disk) {
                    return false
                }
            }
            return true
        }
        return false
    }

    fun clean() {
        discList.forEach {
            for (i in 0..it.lastIndex) {
                it[i] = Constants.EMPTY_SPACE
            }
        }
    }

}
