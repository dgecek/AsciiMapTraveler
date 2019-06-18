package eu.dgecek.asciimaptraveler.model

data class MapStep(
        val x: Int,
        val y: Int,
        val character: Char,
        val direction: MapPathDirection
)
