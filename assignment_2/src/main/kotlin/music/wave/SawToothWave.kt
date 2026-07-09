package src.main.kotlin.music.wave

import src.main.kotlin.music.wave.Wave

class SawToothWave: WaveFormStrategy {
    override fun shape(phase: Double): Double {
        return phase / Math.PI - 1.0
    }
}
