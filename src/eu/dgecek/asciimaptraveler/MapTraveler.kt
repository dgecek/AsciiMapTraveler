package eu.dgecek.asciimaptraveler

import eu.dgecek.asciimaptraveler.model.AsciiMap
import eu.dgecek.asciimaptraveler.model.TravelResult

interface MapTraveler {

    /**
     * Takes ASCII map as an input and outputs the letters and the path that algorithm travelled.
     */
    fun findThePath(asciiMap: AsciiMap): TravelResult
}