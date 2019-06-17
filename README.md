# AsciiMapTraveler

Map Traveler is path following algorithm in ASCII Map. 
'@' represents the start, 'x' represents the end of the path.
 
It takes ASCII map as an input and outputs the letters and the path that algorithm travelled.

Example:

    @
    | C----+
    A |    |
    +---B--+
      |      x
      |      |
      +---D--+

Expected result:

Letters ABCD

Path as characters @|A+---B--+|+----C|-||+---D--+|x

Usage:

    private val mapTraveler: MapTraveler = MapTravelerImpl()

    val result = mapTraveler.findThePath(AsciiMap.fromAsciiMapString(mapString))
    println(result.collectedLetters))
    println(result.path))
