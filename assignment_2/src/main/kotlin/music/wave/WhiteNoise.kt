package src.main.kotlin.music.wave

import src.main.kotlin.music.wave.Wave
import kotlin.random.Random


class WhiteNoise: WaveFormStrategy {
    override fun shape(phase: Double): Double {
        return Random.nextDouble(-1.0, 1.0)
    }
}
