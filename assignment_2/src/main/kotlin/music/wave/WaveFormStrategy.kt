package src.main.kotlin.music.wave

interface WaveFormStrategy {
    fun shape(phase: Double): Double
}