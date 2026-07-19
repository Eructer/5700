package api.programs

import api.RobotApi
import command.MoveCommand
import observer.Observer


class CollisionObserver(private val robot: RobotApi): Observer<Boolean> {
    override fun onUpdate(value: Boolean) {
        if (value) {
            robot.perform(MoveCommand(robot.actuator, 25.0, 50.0))
        }
    }
}