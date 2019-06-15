package eu.dgecek.asciimaptraveler

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


internal class MapTravelerImplTest {

    private val mapTraveler: MapTraveler = MapTravelerImpl()

    @Test
    fun shouldFindThePathInSimpleMap() {
        val mapString = "  @---A---+\n" +
                "          |\n" +
                "  x-B-+   C\n" +
                "      |   |\n" +
                "      +---+"

        val result = mapTraveler.findThePath(AsciiMap(mapString))
        assertTrue(result.collectedLetters == "ACB")
        assertTrue(result.path == "@---A---+|C|+---+|+-B-x")
    }
}