package src.music.wave

import src.music.wave.Wave
import kotlin.random.Random


class WhiteNoise {
    fun shape(phase: Double): Double {
        return Random.nextDouble(-1.0, 1.0)
    }
}