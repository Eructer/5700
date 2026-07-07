package src.music.wave

import src.music.wave.Wave
import kotlin.random.Random


class WhiteNoise(private val frequency: Double, private val sampleRate: Double, private val timeWindow: Double): Wave(frequency, sampleRate, timeWindow) {
    override fun shape(phase: Double): Double {
        return Random.nextDouble(-1.0, 1.0)
    }
}