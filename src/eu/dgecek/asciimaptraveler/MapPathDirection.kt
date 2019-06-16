package eu.dgecek.asciimaptraveler

sealed class MapPathDirection {

    object UP : MapPathDirection()
    object RIGHT : MapPathDirection()
    object DOWN : MapPathDirection()
    object LEFT : MapPathDirection()
    object NONE : MapPathDirection()
}
