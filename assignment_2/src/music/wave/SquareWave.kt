package src.music.wave

import src.music.wave.Wave

class SquareWave(private val frequency: Double, private val sampleRate: Double, private val timeWindow: Double): Wave(frequency, sampleRate, timeWindow) {
    override fun shape(phase: Double): Double {
        if (phase < Math.PI) {
            return 1.0
        } else return -1.0
    }
}