package src.music

import java.io.File
import kotlin.math.pow


class FileHandler() {
    fun readFile(filePath: String): Array<String> {
        val file = File(filePath)

        val text = file.readLines().toTypedArray()

        return text
    }
}