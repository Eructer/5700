package api.programs

import api.RobotApi
import command.MoveCommand
import observer.Observer
import java.awt.Color

class VisionObserver(private val robot: RobotApi): Observer<Color> {
    private val chaseSpeed = 90.0
    private val avoidTurnSpeed = 70.0
    private val searchSpin = 30.0
    private val avoidDistance = 45.0
    
    override fun onUpdate(value: Color) {
        val sonarDistance = robot.sensors.sonar.reading ?: Double.MAX_VALUE

        val (left, right) = when {
            // Obstacle close ahead: turn away first, regardless of what vision sees.
            sonarDistance < avoidDistance -> avoidTurnSpeed to -avoidTurnSpeed
            isBallAhead(value) -> chaseSpeed to chaseSpeed
            // Nothing interesting ahead: spin slowly to scan for the ball.
            else -> -searchSpin to searchSpin
        }
        robot.perform(MoveCommand(robot.actuator, left, right))
    }

    private fun isBallAhead(color: Color): Boolean =
        color.red > 0.6 && color.green < 0.4 && color.blue < 0.4
}