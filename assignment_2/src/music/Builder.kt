package src.music

import src.music.Mixer
import src.music.wave.SineWave
import src.music.effects.*

class Builder {
    fun buildSong(fileContents: Array<String>) {
        val pianoHelper = PianoNotes
        // Header ; First line in file contains 3 space-separated values
        // sampleRate beatsPerMeasure tempo
        val header = fileContents[0].split(" ")
        val sampleRate = header[0]
        val beatsPerMeasure = header[1]
        val tempo = header[2]
        // Channel Lines ; Each line after the header is one channel. A channel line is split by the | character
        // The first segment is the channel's settings; every segment after it is a measure
        // settings|measure1|measure2|measure3|...
        // Channel Settings ; The settings segment is a space separated list. The first token is the waveform to generate for the channel
        // The remaining tokens are effects to apply to the channel, and my appear in any order. A channel may have zero or more effects
        // wavform effect effect ...
        val sinWave = SinWave()
        val channels = fileContents.slice(1 ..< fileContents.size)
        // song = {channels: [channel, channel]}
        var song: MutableMap<String, Any> = mutableMapOf()
        for (channel in channels) {
            // songChannel = {wave : wave, effects : [{effect, effect settings}, {effect, effect settings}], notes : [{note: C4, duration: 1}, {}]}
            // val songChannel: MutableMap<String, Any> = mutableMapOf("wave" to "value", "effects" to mutableListOf<MutableMap<String, Double>>(), "notes" to mutableListOf<MutableMap<String, Double>>())
            
            val firstPipe = channel.indexOf("|")
            
            val settings = channel.slice(0..firstPipe).trim().split(" ")

            val wave = settings[0]

            

            // val effectArray: MutableList<MutableMap<String, Any>> = mutableListOf()
            // for (i in 1 until settings.size) {
            //     val effect = settings[i].split("$")
                
            //     val effectType = effect[0]
                
            //     val effectSettings = effect.slice(0..effect.size).map {note -> note.toInt()}

            //     effectArray.add(mutableMapOf("effect" to effectType, "effectSettings" to effectSettings))
            // }

            // Measure and notes ; Each measure is a space-separated sequence of note/duration pairs. The first item is the note and the second is how long to play it in beats
            // note duration note duration ...
            // duration and note go into the wave
            val measures = channel.slice(firstPipe+1..<channel.length).split("|")

            val notesArray: MutableList<MutableMap<String, Any>> = mutableListOf()
            var incrementDuration = 1
            var incrementNote = 0
            for (j in measures.indices) {
                val note = measures[j + incrementNote]

                val duration = measures[j + incrementDuration]

                // Generate sounds from here

                notesArray.add(mutableMapOf("note" to (pianoHelper.get(note) ?: 0.0), "duration" to duration))

                incrementDuration += 1
                incrementNote += 1
            }
            

            

            
        }

        // Return mixer result


    }

    object PianoNotes {
        val frequencies: Map<String, Double> = buildMap {
            val sharpNames = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
            val flatNames  = listOf("C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B")
    
            for (key in 1..88) {
                val freq = 440.0 * 2.0.pow((key - 49) / 12.0)
                val semitonesFromC0 = key + 8
                val pitchClass = semitonesFromC0 % 12
                val octave = semitonesFromC0 / 12
                put("${sharpNames[pitchClass]}$octave", freq)
                if (flatNames[pitchClass] != sharpNames[pitchClass]) {
                    put("${flatNames[pitchClass]}$octave", freq)
                }
            }
            put("-", 0.0)
        }
    
        operator fun get(note: String): Double? = frequencies[note]
    }
}