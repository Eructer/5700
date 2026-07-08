package src.music.wave

import src.music.wave.Wave

class SawToothWave {
    fun shape(phase: Double): Double {
        return phase / Math.PI - 1.0
    }
}