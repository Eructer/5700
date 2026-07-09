package src.main.kotlin.music.wave

import src.main.kotlin.music.wave.Wave

class SineWave: WaveFormStrategy {
    override fun shape(phase: Double): Double {
        return Math.sin(phase)
    }
}
