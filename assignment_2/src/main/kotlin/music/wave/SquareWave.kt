package src.main.kotlin.music.wave

import src.main.kotlin.music.wave.Wave

class SquareWave {
    fun shape(phase: Double): Double {
        if (phase < Math.PI) {
            return 1.0
        } else return -1.0
    }
}
