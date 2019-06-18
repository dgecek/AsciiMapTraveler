package eu.dgecek.asciimaptraveler.model

inline class AsciiMap(val structure: Array<Array<Char>>) {

    companion object {
        private const val NEW_LINE_CHAR = '\n'

        fun fromAsciiMapString(mapString: String): AsciiMap = AsciiMap(
                mapString.split(NEW_LINE_CHAR)
                        .map { it.toCharArray().toTypedArray() }
                        .toTypedArray())
    }
}

