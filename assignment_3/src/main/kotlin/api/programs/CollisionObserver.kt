package api.programs

import api.RobotApi
import command.MoveCommand
import observer.Observer
import kotlin.random.Random

class CollisionObserver(private val robot: RobotApi): Observer<Boolean> {
    override fun onUpdate(value: Boolean) {
        if (value) {
            robot.perform(MoveCommand(robot.actuator, 0.0, 80.0))
        }
    }
}