package src.music.wave


open class Wave(private val frequency: Double, private val sampleRate: Double = 44100.0, private val timeWindow: Double) {
    fun generateSound(): DoubleArray {
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

    open fun shape(phase: Double): Double {
        return phase
    }

}
