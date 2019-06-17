package eu.dgecek.asciimaptraveler

interface MapTraveler {

    /**
     * Takes ASCII map as an input and outputs the letters and the path that algorithm travelled.
     */
    fun findThePath(asciiMap: AsciiMap): TravelResult
}