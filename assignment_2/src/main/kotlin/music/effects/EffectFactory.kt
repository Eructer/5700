package src.main.kotlin.music.effects

class EffectFactory(
    private val registry: Map<String, (AudioSource, List<Double>, Double) -> AudioSource> = defaultRegistry
) {
    fun create(name: String, params: List<Double>, wrapped: AudioSource, sampleRate: Double): AudioSource {
        val constructor = registry[name]
            ?: throw Exception(name)
        return constructor(wrapped, params, sampleRate)
    }

    companion object {
        val defaultRegistry: Map<String, (AudioSource, List<Double>, Double) -> AudioSource> = mapOf(
            "vol" to { wrapped, params, _ -> Volume(wrapped, params[0]) },
            "ads" to { wrapped, params, sampleRate -> ADS(wrapped, params[0], params[1], params[2], sampleRate) },
            "tanh" to { wrapped, params, _ -> TahnDistortion(wrapped, params[0]) },
            "clip" to { wrapped, params, _ -> ClipDistortion(wrapped, params[0]) }
        )
    }
}