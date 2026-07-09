package src.main.kotlin.music.effects
import kotlin.math.tanh
class TahnDistortion(private var wrapped: AudioSource, private var drive: Double): AudioSource {
    override fun render(): DoubleArray  {
        val samples = wrapped.render()
        return DoubleArray(samples.size) { i -> tanh(samples[i] * drive)}
    }
}