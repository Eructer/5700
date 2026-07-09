package src.main.kotlin.music.effects

interface AudioSource {
    fun render(): DoubleArray
}