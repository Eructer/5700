package src.main.kotlin.music

import src.main.kotlin.music.effects.*

class Render {
    fun renderTrack(track: Track, sampleRate: Double, tempo: Int): DoubleArray {
        val waveToUse = track.waveForm::shape

        val secondsPerBeat = 60.0 / tempo 

        val noteBuffer = track.measure.flatten().map {note -> 
            val durationSeconds = note.duration * secondsPerBeat

            if (note.isRest) {
                DoubleArray((sampleRate * durationSeconds).toInt())
                
            } else {
                
                var source: AudioSource = SignalGenerator(note.note, sampleRate, durationSeconds, waveToUse)

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
}