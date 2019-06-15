package eu.dgecek.asciimaptraveler

interface MapTraveler {

    fun findThePath(asciiMap: AsciiMap): TravelResult
}