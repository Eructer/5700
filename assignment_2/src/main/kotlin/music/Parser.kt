package src.main.kotlin.music

import src.main.kotlin.music.PianoNotes
import src.main.kotlin.music.wave.SineWave
import src.main.kotlin.music.wave.SquareWave
import src.main.kotlin.music.wave.SawToothWave
import src.main.kotlin.music.wave.WaveFormFactory
import src.main.kotlin.music.wave.WhiteNoise
import src.main.kotlin.music.wave.WaveFormStrategy

class Parser(private val waveFormFactory: WaveFormFactory) {
    fun parseTrack(line: String) : Track {
        val firstPipe = line.indexOf("|")

        if (firstPipe == -1) {
            throw MalformedHeaderException("Track line is missing '|' delimiter: \"$line\"")
        }
        
        val header = line.substring(0, firstPipe).trim()
        
        val body = line.substring(firstPipe)

        val headerParts = header.split(Regex("\\s+"))
        val waveForm = headerParts[0]
        var waveToUse: WaveFormStrategy = waveFormFactory.create(waveForm)

        val params: List<EffectSpec> = headerParts.drop(1).map { part ->
            val segments = part.split("$")
            val name = segments[0]
            val values = segments.drop(1).map { it.toDouble() ?: throw MalformedHeaderException("Invalid effect parameter : '$it' in '$part'") }
            EffectSpec(name, values)
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

            if (i + 1 >= tokens.size) {
                throw MalformedNoteException("Note '$noteName' is missing a duration value")
            }
            
            val duration = tokens[i + 1].toDoubleOrNull() ?: throw MalformedNoteException("Invalid duration for note '$noteName'")
            
            if (noteName == "-") {
                notes.add(Note(isRest = true, note = 0.0, duration = duration))
            } else {
                val regexFind: String = Regex("^([A-G][#b]?)(\\d+)$").find(noteName)?.value ?: throw UnknownNoteException("Invalid note token : '$noteName'")
                val match = pianoHelper?.get(regexFind) ?: throw UnknownNoteException("Unable to get note : $noteName")
    
                notes.add(
                    Note(
                        isRest = false,
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