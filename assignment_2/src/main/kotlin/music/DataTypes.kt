package src.main.kotlin.music

import src.main.kotlin.music.wave.WaveFormStrategy

data class Song (
        val sampleRate: Int,
        val beatsPerMeasure: Int,
        val temp: Int,
        val tracks: List<Track>
    )

data class Track (
        val waveForm: WaveFormStrategy,
        val params: Map<String, List<Double>>,
        val measure: List<List<Note>>
    )

data class Note (
        val isRest: Boolean,
        val note: Double,
        val duration: Double
    )