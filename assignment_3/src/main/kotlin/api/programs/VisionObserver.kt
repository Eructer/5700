package api.programs

import api.RobotApi
import command.MoveCommand
import observer.Observer
import javafx.scene.paint.Color

class VisionObserver(private val robot: RobotApi): Observer<Color> {
    private val chaseSpeed = 90.0
      private val scanForward = 40.0
      private val scanTurn = 70.0
      private val wigglePeriodTicks = 15
   
      private var tick = 0
   
      override fun onUpdate(value: Color) {
          tick++
          val (left, right) = if (isBallAhead(value)) {
              chaseSpeed to chaseSpeed
          } else if (isObstacleAhead(value)) {
              avoid()
          } else if (isEmptySpace(value)) {
              chaseSpeed to chaseSpeed
          } else {
              scan()
          }
          robot.perform(MoveCommand(robot.actuator, left, right))
      }
   
      private fun scan(): Pair<Double, Double> {
          val phase = (tick / wigglePeriodTicks) % 2
          return if (phase == 0) scanForward - scanTurn to scanForward + scanTurn
          else scanForward + scanTurn to scanForward - scanTurn
      }

      private fun avoid(): Pair<Double, Double> {
          return -50.0 to 50.0
      }
   
      private fun isBallAhead(color: Color): Boolean =
          color.red > 0.6 && color.green < 0.4 && color.blue < 0.4
      private fun isObstacleAhead(color: Color): Boolean =
            color.red > 0.6 && color.green > 0.7 && color.blue > 0.8
    private fun isEmptySpace(color: Color): Boolean =
            color.red < 0.4 && color.green < 0.4 && color.blue < 0.5
}