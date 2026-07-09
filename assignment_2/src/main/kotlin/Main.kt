package src.main.kotlin

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.SourceDataLine

import src.main.kotlin.music.Builder
import src.main.kotlin.music.FileHandler
import java.io.File

fun playSong(samples: DoubleArray, sampleRate: Double) {
    // 16-bit, mono, signed, little-endian PCM
    val format = AudioFormat(sampleRate.toFloat(), 16, 1, true, false)
    val line: SourceDataLine = AudioSystem.getSourceDataLine(format)
    line.open(format)
    line.start()

    // Convert each Double in [-1.0, 1.0] into two bytes (a 16-bit signed sample).
    val buffer = ByteArray(samples.size * 2)
    for (i in samples.indices) {
        val clamped = samples[i].coerceIn(-1.0, 1.0)
        val value = (clamped * Short.MAX_VALUE).toInt()
        buffer[i * 2] = (value and 0xFF).toByte()          // low byte
        buffer[i * 2 + 1] = (value shr 8 and 0xFF).toByte()    // high byte
    }

    line.write(buffer, 0, buffer.size)
    line.drain()   // block until every sample has finished playing
    line.stop()
    line.close()
}


fun main() {
    // load file
    val fileName = "src/resources/test_files/songs/zelda_theme.txt"
    val file = FileHandler()
    val content = file.readFile(fileName)
    // parse file and mix channels
    val songBuilder = Builder()
    val song = songBuilder.buildSong(content)
    // play mix via playSong
    playSong(song.first, song.second)

}
