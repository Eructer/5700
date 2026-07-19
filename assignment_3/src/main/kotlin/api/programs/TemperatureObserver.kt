package api.programs

import api.RobotApi
import command.MoveCommand
import observer.Observer

class TemperatureObserver(private val robot: RobotApi): Observer<Double> {

    private var previousValue: Double = 0.0

    private var maxSeen: Double = 0.0

    override fun onUpdate(value: Double) {

        if (value >= maxSeen) {
            maxSeen = value
            robot.perform(MoveCommand(robot.actuator, 0.0, 0.0))
        } 
         else if (value < previousValue) {
            robot.perform(MoveCommand(robot.actuator, -25.0, -50.0))
        } else {
            robot.perform(MoveCommand(robot.actuator, 25.0, 50.0))
        }
        
        previousValue = value
        
    }
}