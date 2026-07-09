package src.main.kotlin.music.effects

class ADS(private var wrapped: AudioSource, private val attackEnd: Double, private val decayEnd: Double, private val sustain: Double, private var sampleRate: Double): AudioSource {
    override fun render(): DoubleArray {
        val samples = wrapped.render()
        val attackSamples = attackEnd * sampleRate
        val decaySamples = decayEnd * sampleRate
        var result = DoubleArray(samples.size)
        for (n in samples.indices) {
            if (n < attackSamples) {
                result[n] = samples[n] * (n / attackSamples)
            } else if (n < attackSamples + decaySamples) {
                result[n] = samples[n] * (1.0 + (sustain - 1.0) * ((n - attackSamples) / decaySamples))
            } else {
                result[n] = samples[n] * sustain
            }
        }
        return result
    }
}