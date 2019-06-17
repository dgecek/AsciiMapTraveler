package eu.dgecek.asciimaptraveler

inline class AsciiMap(
        val asciiMapLines: List<String>) {

    companion object {
        private const val NEW_LINE_CHAR = '\n'

        fun fromAsciiMapString(mapString: String): AsciiMap = AsciiMap(mapString.split(NEW_LINE_CHAR))
    }
}

