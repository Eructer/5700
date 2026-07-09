package src.main.kotlin.music

import src.main.kotlin.music.wave.Wave
import src.main.kotlin.music.Parser
import src.main.kotlin.music.Render
import src.main.kotlin.music.effects.EffectFactory
import src.main.kotlin.music.wave.WaveFormFactory

class Builder {
    fun buildSong(fileContents: String): Pair<DoubleArray, Double> {
        val pianoHelper = PianoNotes
        
        val wave = Wave()

        val parser = Parser(WaveFormFactory())

        val effectFactory = EffectFactory()

        val render = Render(effectFactory)

        val lines = fileContents.trim().lines().filter { it.isNotBlank()}

        val (sampleRate, beatsPerMeasure, tempo) = lines[0].trim().split(Regex("\\s+")).map { it.toInt() }

        if (sampleRate <= 0) throw MalformedHeaderException("Sample rate is 0 or less")
        if (beatsPerMeasure <= 0) throw MalformedHeaderException("Beats Per Measure is 0 or less")
        if (tempo <= 0) throw MalformedHeaderException("Temp is 0 or less")

        
        val tracks = lines.drop(1).map { parser.parseTrack(it.trim())}.map { render.renderTrack(it, sampleRate.toDouble(), tempo)}

        if (tracks.isEmpty()) throw MalformedTrackException("No tracks parsed")
        
        val mixer = Mixer()
        
        return Pair(mixer.mix(tracks), sampleRate.toDouble())
    }
}