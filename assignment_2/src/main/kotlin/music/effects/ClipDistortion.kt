package src.main.kotlin.music.effects

class ClipDistortion(private var wrapped: AudioSource, private var threshold: Double): AudioSource {
    override fun render(): DoubleArray {
        val samples = wrapped.render()
        return DoubleArray(samples.size) { n -> samples[n].coerceIn(-threshold, threshold)}
    }
}