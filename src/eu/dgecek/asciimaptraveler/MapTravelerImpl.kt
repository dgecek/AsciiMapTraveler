package eu.dgecek.asciimaptraveler

import java.lang.IllegalArgumentException

class MapTravelerImpl : MapTraveler {

    companion object {
        private const val START_CHAR = '@'
        private const val NEW_LINE_CHAR = '\n'
        private const val END_CHAR = 'x'
        private const val CROSSROAD_CHAR = '+'
    }

    override fun findThePath(asciiMap: AsciiMap): TravelResult {
        val startPosition = findStartOfThePath(asciiMap)
        return findPathRecursively(asciiMap, mutableListOf(startPosition))
    }
    
    private fun findStartOfThePath(asciiMap: AsciiMap): MapStep {
        var line = 0
        var column = 0
        val mapString = asciiMap.mapString

        mapString.forEach { char ->
            when (char) {
                START_CHAR -> {
                    return MapStep(column, line, char)
                }
                NEW_LINE_CHAR -> {
                    line++
                    column = 0
                }
                else -> {
                    column++
                }
            }
        }

        throw IllegalArgumentException("AsciiMap doesn't contain @ as start char.")
    }

    private fun findPathRecursively(asciiMap: AsciiMap, mapSteps: MutableList<MapStep>): TravelResult {
        if (mapSteps.isEmpty()) {
            throw IllegalArgumentException("Start character required in mapSteps list.")
        }
        val lastStep = mapSteps[mapSteps.size - 1]
        return when (lastStep.character) {
            END_CHAR -> travelResultFromMapSteps(mapSteps)
            START_CHAR -> findInitialDirection(asciiMap, mapSteps)
            CROSSROAD_CHAR -> findNextDirection(asciiMap, mapSteps)
            else -> continueOnPath(asciiMap, mapSteps)
        }
    }

    private fun findInitialDirection(asciiMap: AsciiMap, mapSteps: MutableList<MapStep>): TravelResult {
        if (mapSteps.size != 1 || mapSteps[0].character != START_CHAR) {
            throw IllegalArgumentException("Cannot find initial direction when not in initial state. " +
                    "There should be only one @ character in the map.")
        }

        val currentX = mapSteps[0].x
        val currentY = mapSteps[0].y


        val upperChar = getCharacterAtMapPosition(asciiMap, currentX, currentY - 1)
        if (upperChar != null) {
            mapSteps.add(MapStep(currentX, currentY - 1, upperChar))
            return findPathRecursively(asciiMap, mapSteps)
        }

        val bottomChar = getCharacterAtMapPosition(asciiMap, currentX, currentY + 1)
        if (bottomChar != null) {
            mapSteps.add(MapStep(currentX, currentY + 1, bottomChar))
            return findPathRecursively(asciiMap, mapSteps)
        }


    }

    private fun findNextDirection(asciiMap: AsciiMap, mapSteps: MutableList<MapStep>): TravelResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun continueOnPath(asciiMap: AsciiMap, mapSteps: MutableList<MapStep>): TravelResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun travelResultFromMapSteps(mapSteps: MutableList<MapStep>): TravelResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getCharacterAtMapPosition(asciiMap: AsciiMap, x: Int, y: Int): Char? {
        val lines = asciiMap.mapString.split(NEW_LINE_CHAR)
        val line = lines.getOrNull(y)
        return line?.getOrNull(x)
    }
}