package src.main.kotlin.music.effects

import src.main.kotlin.music.wave.Wave

class SignalGenerator(private val frequency: Double, private val sampleRate: Double, private val duration: Double, private val shape: (Double) -> Double): AudioSource {
    override fun render(): DoubleArray {
        return Wave().generateSound(frequency, sampleRate, duration, shape)
    }
}