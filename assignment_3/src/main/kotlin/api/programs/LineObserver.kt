package api.programs

import api.RobotApi
import command.MoveCommand
import observer.Observer


class LineObserver(private val robot: RobotApi): Observer<Boolean> {
    override fun onUpdate(value: Boolean) {
        if (value) {
            robot.perform(MoveCommand(robot.actuator, 2.0, 2.0))
        } else {
            robot.perform(MoveCommand(robot.actuator, 0.0, 0.0))
        }
    }
}