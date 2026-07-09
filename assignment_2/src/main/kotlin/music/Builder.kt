package src.main.kotlin.music

import src.main.kotlin.music.wave.Wave
import src.main.kotlin.music.Parser
import src.main.kotlin.music.Render

class Builder {
    fun buildSong(fileContents: String): Pair<DoubleArray, Double> {
        val pianoHelper = PianoNotes
        
        val wave = Wave()

        val parser = Parser()

        val render = Render()

        val lines = fileContents.trim().lines().filter { it.isNotBlank()}

        val (sampleRate, beatsPerMeasure, tempo) = lines[0].trim().split(Regex("\\s+")).map { it.toInt() }
        
        val tracks = lines.drop(1).map { parser.parseTrack(it.trim())}.map { render.renderTrack(it, sampleRate.toDouble(), tempo)}

        val mixer = Mixer()
        
        return Pair(mixer.mix(tracks), sampleRate.toDouble())
    }
}