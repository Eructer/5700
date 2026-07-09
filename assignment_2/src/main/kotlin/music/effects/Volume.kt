package src.main.kotlin.music.effects

class Volume(private val wrapped: AudioSource, private var level: Double) : AudioSource {
    override fun render(): DoubleArray {
        val samples = wrapped.render()
        return DoubleArray(samples.size) { n -> samples[n] * level}
    }
}
