package src.main.kotlin.music.wave

import src.main.kotlin.music.wave.SineWave
import src.main.kotlin.music.wave.SquareWave
import src.main.kotlin.music.wave.SawToothWave
import src.main.kotlin.music.wave.WhiteNoise

class WaveFormFactory(private val registry: Map<String, () -> WaveFormStrategy> = defaultRegistry) {
    fun create(token: String): WaveFormStrategy {
        val constructor = registry[token] ?: throw Exception(token)
        return constructor()
    }

    companion object {
        val defaultRegistry: Map<String, () -> WaveFormStrategy> = mapOf(
            "sin" to { SineWave() },
            "square" to { SquareWave() },
            "saw" to { SawToothWave() },
            "whitenoise" to { WhiteNoise() }
        )
    }
}