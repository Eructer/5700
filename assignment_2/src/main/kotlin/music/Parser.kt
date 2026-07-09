package src.main.kotlin.music

import src.main.kotlin.music.PianoNotes
import src.main.kotlin.music.wave.SineWave
import src.main.kotlin.music.wave.SquareWave
import src.main.kotlin.music.wave.SawToothWave
import src.main.kotlin.music.wave.WhiteNoise
import src.main.kotlin.music.wave.WaveFormStrategy

class Parser {
    fun parseTrack(line: String) : Track {
        val firstPipe = line.indexOf("|")
        val header = line.substring(0, firstPipe).trim()
        val body = line.substring(firstPipe)

        val headerParts = header.split(Regex("\\s+"))
        val waveForm = headerParts[0]
        var waveToUse: WaveFormStrategy = WhiteNoise()
        when (waveForm) {
            "sin" -> waveToUse = SineWave()
            "square" -> waveToUse = SquareWave()
            "saw" -> waveToUse = SawToothWave()
            "whitenoise" -> waveToUse = WhiteNoise()
        }

        val params: Map<String, List<Double>> = headerParts.drop(1).associate {part ->
            val segments = part.split("$")
            val name = segments[0]
            val values = segments.drop(1).map { it.toDouble() }
            name to values
        }

        val measures = body.split("|").map {it.trim()}.filter{it.isNotEmpty()}.map{parseMeasure(it)}

        return Track(waveToUse, params, measures)
        
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
}