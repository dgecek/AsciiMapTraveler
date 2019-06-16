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
            START_CHAR -> findFirstStep(asciiMap, mapSteps)
            CROSSROAD_CHAR -> findNextStepOnCrossroad(asciiMap, mapSteps)
            else -> continueOnPath(asciiMap, mapSteps)
        }
    }

    private fun findFirstStep(asciiMap: AsciiMap, mapSteps: MutableList<MapStep>): TravelResult {
        if (mapSteps.size != 1 || mapSteps[0].character != START_CHAR) {
            throw IllegalArgumentException("Cannot find initial direction when not in initial state. " +
                    "There should be only one @ character in the map.")
        }

        val currentX = mapSteps[0].x
        val currentY = mapSteps[0].y

        val upStep = getMapStepAtPosition(asciiMap, currentX, currentY - 1, MapPathDirection.UP)
        val downStep = getMapStepAtPosition(asciiMap, currentX, currentY + 1, MapPathDirection.DOWN)
        val leftStep = getMapStepAtPosition(asciiMap, currentX - 1, currentY, MapPathDirection.LEFT)
        val rightStep = getMapStepAtPosition(asciiMap, currentX + 1, currentY, MapPathDirection.RIGHT)

        val legitSteps = listOfNotNull(upStep, downStep, leftStep, rightStep)

        if (legitSteps.isEmpty()) {
            throw IllegalArgumentException("Cannot find initial direction.")
        }
        mapSteps.add(legitSteps[0])
        return findPathRecursively(asciiMap, mapSteps)
    }

    private fun findNextStepOnCrossroad(asciiMap: AsciiMap, mapSteps: MutableList<MapStep>): TravelResult {
        if (mapSteps.isEmpty()) {
            throw IllegalArgumentException("Got empty mapSteps on crossroad.")
        }
        


    }

    private fun continueOnPath(asciiMap: AsciiMap, mapSteps: MutableList<MapStep>): TravelResult {
        if (mapSteps.isEmpty()) {
            throw IllegalArgumentException("Got empty mapSteps on path.")
        }

        val lastStep = mapSteps[mapSteps.size - 1]

        return when (lastStep.direction) {
            MapPathDirection.UP -> continueOnPathInDirection(asciiMap, mapSteps, lastStep.x, lastStep.y - 1, MapPathDirection.UP)
            MapPathDirection.RIGHT -> continueOnPathInDirection(asciiMap, mapSteps, lastStep.x + 1, lastStep.y, MapPathDirection.RIGHT)
            MapPathDirection.DOWN -> continueOnPathInDirection(asciiMap, mapSteps, lastStep.x, lastStep.y + 1, MapPathDirection.DOWN)
            MapPathDirection.LEFT -> continueOnPathInDirection(asciiMap, mapSteps, lastStep.x - 1, lastStep.y, MapPathDirection.LEFT)
            MapPathDirection.NONE -> throwPathCutException()
        }
    }

    private fun continueOnPathInDirection(asciiMap: AsciiMap, mapSteps: MutableList<MapStep>,
                                          x: Int, y: Int, direction: MapPathDirection): TravelResult =
            getMapStepAtPosition(asciiMap, x, y, direction)?.let {
                mapSteps.add(it)
                findPathRecursively(asciiMap, mapSteps)
            } ?: throwPathCutException()

    private fun throwPathCutException(): TravelResult = throw IllegalArgumentException("Path is cut. Illegal map given.")


    private fun travelResultFromMapSteps(mapSteps: MutableList<MapStep>): TravelResult {
        return TravelResult(
                mapSteps.filter { it.character.isLetter() }
                        .map { it.character }
                        .joinToString(),
                mapSteps.map { it.character }
                        .joinToString { }
        )
    }

    private fun getMapStepAtPosition(asciiMap: AsciiMap, x: Int, y: Int, direction: MapPathDirection): MapStep? {
        val lines = asciiMap.mapString.split(NEW_LINE_CHAR)
        val line = lines.getOrNull(y)
        val char = line?.getOrNull(x)
        return char?.let {
            if (char.isWhitespace()) null else MapStep(x, y, it, direction)
        }
    }
}