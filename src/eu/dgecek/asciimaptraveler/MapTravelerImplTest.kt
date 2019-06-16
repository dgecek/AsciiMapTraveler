package eu.dgecek.asciimaptraveler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


internal class MapTravelerImplTest {

    private val mapTraveler: MapTraveler = MapTravelerImpl()

    @Test
    fun shouldFindThePathInSimpleMap() {
        val mapString =
                "  @---A---+\n" +
                "          |\n" +
                "  x-B-+   C\n" +
                "      |   |\n" +
                "      +---+"

        val result = mapTraveler.findThePath(AsciiMap(mapString))
        assertEquals("ACB", result.collectedLetters)
        assertEquals("@---A---+|C|+---+|+-B-x", result.path)
    }

    @Test
    fun shouldFindThePathInMapWithPathGoingOverItself() {
        val mapString =
                "  @\n" +
                "  | C----+\n" +
                "  A |    |\n" +
                "  +---B--+\n" +
                "    |      x\n" +
                "    |      |\n" +
                "    +---D--+"

        val result = mapTraveler.findThePath(AsciiMap(mapString))
        assertEquals("ABCD", result.collectedLetters)
        assertEquals("@|A+---B--+|+----C|-||+---D--+|x", result.path)
    }

    @Test
    fun shouldFindThePathWhenLetterIsTwiceOnPath(){
        val mapString =
                "  @---+\n" +
                "      B\n" +
                "K-----|--A\n" +
                "|     |  |\n" +
                "|  +--E  |\n" +
                "|  |     |\n" +
                "+--E--Ex C\n" +
                "   |     |\n" +
                "   +--F--+"

        val result = mapTraveler.findThePath(AsciiMap(mapString))
        assertEquals("BEEFCAKE", result.collectedLetters)
        assertEquals("@---+B||E--+|E|+--F--+|C|||A--|-----K|||+--E--Ex", result.path)
    }
}