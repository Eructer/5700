package src.main.kotlin.music

import src.main.kotlin.music.wave.WaveFormStrategy

sealed class SongParseException(message: String) : Exception(message)

class MalformedTrackException(message: String) : SongParseException(message)
class MalformedNoteException(message: String) : SongParseException(message)
class MalformedHeaderException(message: String) : SongParseException(message)
class UnknownNoteException(noteName: String) : SongParseException("Unknown note or invalid note name: '$noteName'")

data class Song (
        val sampleRate: Int,
        val beatsPerMeasure: Int,
        val temp: Int,
        val tracks: List<Track>
    )

    data class EffectSpec (
        val name: String,
        val values: List<Double>
    )

data class Track (
    val waveForm: WaveFormStrategy,
    val effects: List<EffectSpec>,
    val measure: List<List<Note>>
    )

data class Note (
        val isRest: Boolean,
        val note: Double,
        val duration: Double
    )