package src.music.wave

import src.music.wave.Wave

class SquareWave {
    fun shape(phase: Double): Double {
        if (phase < Math.PI) {
            return 1.0
        } else return -1.0
    }
}