package eu.dgecek.asciimaptraveler

import eu.dgecek.asciimaptraveler.model.AsciiMap
import eu.dgecek.asciimaptraveler.model.MapPathDirection
import eu.dgecek.asciimaptraveler.model.MapStep
import eu.dgecek.asciimaptraveler.model.TravelResult
import java.lang.IllegalArgumentException

class MapTravelerImpl : MapTraveler {

    companion object {
        private const val START_CHAR = '@'
        private const val END_CHAR = 'x'
        private const val CROSSROAD_CHAR = '+'
    }

    override fun findThePath(asciiMap: AsciiMap): TravelResult {
        val startPosition = findStartOfThePath(asciiMap)
        return findPathRecursively(asciiMap, mutableListOf(startPosition))
    }

    private fun findStartOfThePath(asciiMap: AsciiMap): MapStep {
        val lines = asciiMap.structure

        lines.forEachIndexed { lineIndex, line ->
            line.forEachIndexed { columnIndex, char ->
                if (char == START_CHAR) {
                    return MapStep(columnIndex, lineIndex, char, MapPathDirection.NONE)
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
            else -> {
                if (lastStep.character.isLetter()) {
                    //letters can be crossroads
                    findNextStepOnCrossroad(asciiMap, mapSteps)
                } else {
                    continueOnPath(asciiMap, mapSteps)
                }
            }
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

        val lastStep = mapSteps[mapSteps.size - 1]
        val lastDirection = lastStep.direction

        //don't go back
        val upStep = if (lastDirection == MapPathDirection.DOWN) null else getMapStepAtPosition(asciiMap, lastStep.x, lastStep.y - 1, MapPathDirection.UP)
        val downStep = if (lastDirection == MapPathDirection.UP) null else getMapStepAtPosition(asciiMap, lastStep.x, lastStep.y + 1, MapPathDirection.DOWN)
        val leftStep = if (lastDirection == MapPathDirection.RIGHT) null else getMapStepAtPosition(asciiMap, lastStep.x - 1, lastStep.y, MapPathDirection.LEFT)
        val rightStep = if (lastDirection == MapPathDirection.LEFT) null else getMapStepAtPosition(asciiMap, lastStep.x + 1, lastStep.y, MapPathDirection.RIGHT)

        val legitSteps = listOfNotNull(upStep, downStep, leftStep, rightStep)

        if (legitSteps.isEmpty() || legitSteps.size == 2) {
            throw IllegalArgumentException("Cannot find direction on crossroad.")
        }

        val nextStep =
                if (legitSteps.size > 1) {
                    //try to go straight
                    when (lastDirection) {
                        MapPathDirection.UP -> upStep
                        MapPathDirection.DOWN -> downStep
                        MapPathDirection.LEFT -> leftStep
                        MapPathDirection.RIGHT -> rightStep
                        else -> legitSteps[0]
                    }
                } else {
                    legitSteps[0]
                } ?: legitSteps[0]

        mapSteps.add(nextStep)
        return findPathRecursively(asciiMap, mapSteps)
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
            MapPathDirection.NONE -> throwPathCutException(mapSteps)
        }
    }

    private fun continueOnPathInDirection(asciiMap: AsciiMap, mapSteps: MutableList<MapStep>,
                                          x: Int, y: Int, direction: MapPathDirection): TravelResult =
            getMapStepAtPosition(asciiMap, x, y, direction)?.let {
                mapSteps.add(it)
                findPathRecursively(asciiMap, mapSteps)
            } ?: throwPathCutException(mapSteps)

    private fun throwPathCutException(mapSteps: MutableList<MapStep>): TravelResult =
            throw IllegalArgumentException("Path is cut. Illegal map given. Steps: $mapSteps")

    private fun travelResultFromMapSteps(mapSteps: MutableList<MapStep>): TravelResult {
        return TravelResult(
                mapSteps.filter { it.character.isLetter() && it.character != END_CHAR }
                        .distinctBy { Pair(it.x, it.y) }
                        .map { it.character }
                        .joinToString(separator = ""),
                mapSteps.map { it.character }
                        .joinToString(separator = "")
        )
    }

    private fun getMapStepAtPosition(asciiMap: AsciiMap, x: Int, y: Int, direction: MapPathDirection): MapStep? {
        val lines = asciiMap.structure
        val line = lines.getOrNull(y)
        val char = line?.getOrNull(x)
        return char?.let {
            if (char.isWhitespace()) null else MapStep(x, y, it, direction)
        }
    }
}