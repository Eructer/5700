package src.music.wave

import src.music.wave.Wave

class SawToothWave(private val frequency: Double, private val sampleRate: Double, private val timeWindow: Double): Wave {
    override fun generateSound() {
        println("SawTooth Wave")
    }
}