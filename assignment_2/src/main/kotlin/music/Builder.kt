package src.main.kotlin.music

import src.main.kotlin.music.Mixer
import src.main.kotlin.music.wave.SineWave
import src.main.kotlin.music.wave.SquareWave
import src.main.kotlin.music.wave.SawToothWave
import src.main.kotlin.music.wave.WhiteNoise
import src.main.kotlin.music.wave.Wave
import src.main.kotlin.music.effects.*
import kotlin.math.pow

class Builder {
    fun buildSong(fileContents: String): Pair<DoubleArray, Double> {
        val pianoHelper = PianoNotes
        val wave = Wave()

        val lines = fileContents.trim().lines().filter { it.isNotBlank()}

        val (sampleRate, beatsPerMeasure, tempo) = lines[0].trim().split(Regex("\\s+")).map { it.toInt() }
        
        val tracks = lines.drop(1).map { parseTrack(it.trim())}.map { renderTrack(it, sampleRate.toDouble(), tempo)}

        val mixer = Mixer()
        
        return Pair(mixer.mix(tracks), sampleRate.toDouble())

    }

    

    private fun renderTrack(track: Track, sampleRate: Double, tempo: Int): DoubleArray {
        val waveForm = track.waveForm
        var waveToUse: (Double) -> Double = WhiteNoise()::shape // White Noise default
        when (waveForm) {
            "sin" -> waveToUse = SineWave()::shape
            "square" -> waveToUse = SquareWave()::shape
            "saw" -> waveToUse = SawToothWave()::shape
            "whitenoise" -> waveToUse = WhiteNoise()::shape
        }

        val secondsPerBeat = 60.0 / tempo 

        val noteBuffer = track.measure.flatten().map {note -> 
            val durationSeconds = note.duration * secondsPerBeat

            if (note.isRest) {
                DoubleArray((sampleRate * durationSeconds).toInt())
            } else {
                // Apply effects here
                var source: AudioSource = Effects(note.note, sampleRate, durationSeconds, waveToUse)
                for (effect in track.params) {
                    val currentEffect = effect.key
                    source = when (currentEffect) {
                        "vol" -> Volume(source, effect.value[0])
                        "ads" -> ADS(source, effect.value[0], effect.value[1], effect.value[2], sampleRate)
                        "tanh" -> TahnDistortion(source, effect.value[0])
                        "clip" -> ClipDistortion(source, effect.value[0])
                        else -> source
                    }
                }
                source.render()
            }
        }

        val totalSamples = noteBuffer.sumOf { it.size }
        val trackBuffer = DoubleArray(totalSamples)
        var offset = 0
        for (buf in noteBuffer) {
            buf.copyInto(trackBuffer, offset)
            offset += buf.size
        }
        return trackBuffer
    }

    private fun parseTrack(line: String) : Track {
        val firstPipe = line.indexOf("|")
        val header = line.substring(0, firstPipe).trim()
        val body = line.substring(firstPipe)

        val headerParts = header.split(Regex("\\s+"))
        val waveForm = headerParts[0]

        val params: Map<String, List<Double>> = headerParts.drop(1).associate {part ->
            val segments = part.split("$")
            val name = segments[0]
            val values = segments.drop(1).map { it.toDouble() }
            name to values
        }

        val measures = body.split("|").map {it.trim()}.filter{it.isNotEmpty()}.map{parseMeasure(it)}

        return Track(waveForm, params, measures)
        
    }

    private fun parseMeasure(measureString: String): List<Note> {
        val tokens = measureString.split(Regex("\\s+"))
        val notes = mutableListOf<Note>()
        val pianoHelper = PianoNotes

        var i = 0
        while (i < tokens.size) {
            val noteName = tokens[i]
            val duration = tokens[i + 1].toDouble()

            var isRest = false
            
            if (noteName == "-") {
                isRest = true
            } else {
                val regexFind: String = Regex("^([A-G][#b]?)(\\d+)$").find(noteName)?.value ?: ""
                val match = pianoHelper?.get(regexFind) ?: 0.0

                notes.add(
                    Note(
                        isRest = isRest,
                        note = match,
                        duration = duration
                    )
                )
            }
            i += 2
        }
        return notes
    }

    data class Song (
        val sampleRate: Int,
        val beatsPerMeasure: Int,
        val temp: Int,
        val tracks: List<Track>
    )

    data class Track (
        val waveForm: String,
        val params: Map<String, List<Double>>,
        val measure: List<List<Note>>
    )

    data class Note (
        val isRest: Boolean,
        val note: Double,
        val duration: Double
    )
}