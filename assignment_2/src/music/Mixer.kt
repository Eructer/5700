package src.music

import kotlin.math.pow
import kotlin.math.abs


class Mixer {

    fun mix(channels: List<DoubleArray>): DoubleArray {
        val length = channels.maxOf {it.size}
        val mixed = DoubleArray(length)
        for (channel in channels) {
            for (n in channel.indices) {
                mixed[n] += channel[n]
            }
        }
        return normalize(mixed)
    }

    private fun normalize(samples: DoubleArray): DoubleArray {
        val peak = samples.maxOfOrNull { abs(it) } ?: 0.0
        if (peak == 0.0 || peak <= 1.0) return samples
        return DoubleArray(samples.size) { samples[it] / peak}
    }

    private fun getEffect(effectType: String) {
        TODO("Impliment effects")
    }



}