package src.music.wave

import src.music.wave.Wave

class SineWave(private val frequency: Double, private val sampleRate: Double, private val timeWindow: Double): Wave {
    override fun generateSound() {
        println("Sine Wave")
    }
}