package src.main.kotlin.music.wave


open class Wave() {
    fun generateSound(
        frequency: Double,
        sampleRate: Double = 44100.0,
        timeWindow: Double,
        shape: (phase: Double) -> Double
    ): DoubleArray {
        val totalSamples = sampleRate * timeWindow
        val phaseIncrement = 2 * Math.PI * frequency / sampleRate
        val samples = DoubleArray(totalSamples.toInt()) { 0.0 }
        var phase = 0.0
        for (n in 0 until samples.size) {
            samples[n] = shape(phase)
            phase += phaseIncrement
            if (phase >= 2.0 * Math.PI) phase -= 2.0 * Math.PI
        }

        return samples
    }

}
