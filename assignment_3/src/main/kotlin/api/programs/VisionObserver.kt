package api.programs

import api.RobotApi
import command.MoveCommand
import observer.Observer
import java.awt.Color

class VisionObserver(private val robot: RobotApi): Observer<Color> {
    override fun onUpdate(value: Color) {
        TODO("Not yet implemented")
    }
}