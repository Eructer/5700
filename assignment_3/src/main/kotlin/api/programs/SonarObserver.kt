package api.programs

import api.RobotApi
import command.MoveCommand
import observer.Observer

class SonarObserver(private val robot: RobotApi): Observer<Double> {
    override fun onUpdate(value: Double) {
        if (value > 0.0) {
            robot.perform(MoveCommand(robot.actuator, 2.0, 2.0))
        } else {
            robot.perform(MoveCommand(robot.actuator, 0.0, 0.0))
        }
    }
}