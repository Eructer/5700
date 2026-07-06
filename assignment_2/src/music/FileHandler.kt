package src.music

import java.io.File


class FileHandler() {
    fun readFile(filePath: String): Array<String> {
        val file = File(filePath)

        val text = file.readLines().toTypedArray()

        return text
    }

    fun parseFile(fileContents: Array<String>) {
        // Header ; First line in file contains 3 space-separated values
        // sampleRate beatsPerMeasure tempo


        // Channel Lines ; Each line after the header is one channel. A channel line is split by the | character
        // The first segment is the channel's settings; every segment after it is a measure
        // settings|measure1|measure2|measure3|...


        // Channel Settings ; The settings segment is a space separated list. The first token is the waveform to generate for the channel
        // The remaining tokens are effects to apply to the channel, and my appear in any order. A channel may have zero or more effects
        // wavform effect effect ...


        // Measure and notes ; Each measure is a space-separated sequence of note/duration pairs. The first item is the note and the second is how long to play it in beats
        // note duration note duration ...
    }
}