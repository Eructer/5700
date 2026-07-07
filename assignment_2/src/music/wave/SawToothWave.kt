package src.music.wave

import src.music.wave.Wave

class SawToothWave(private val frequency: Double, private val sampleRate: Double, private val timeWindow: Double): Wave(frequency, sampleRate, timeWindow) {
    override fun shape(phase: Double): Double {
        return phase / Math.PI - 1.0
    }
}