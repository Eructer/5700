package api.programs

import api.RobotApi
import command.MoveCommand
import observer.Observer


class LineObserver(private val robot: RobotApi): Observer<Boolean> {

    private val baseSpeed = 60.0
      private val turnBoost = 45.0
      private val wiggleTurn = 12.0
      private val wigglePeriodTicks = 15
      private val searchForward = 25.0
      private val searchTurn = 45.0
      private val gracePeriodTicks = 5
   
      // -1 = line last confirmed to the left, +1 = last confirmed to the right. Starts at an
      // arbitrary non-zero bias so even a loss on the very first tick has a direction to curve
      // toward. Only ever updated by a real outer-sensor reading, never by the wiggle itself.
      private var lastSide = 1
      private var ticksLost = 0
      private var tick = 0
   
      override fun onUpdate(value: Boolean) {
          val left = robot.sensors.lineLeft.reading ?: false
          val center = robot.sensors.lineCenter.reading ?: false
          val right = robot.sensors.lineRight.reading ?: false
          tick++
   
          val (l, r) = when {
              // A confirmed outer-sensor reading is trustworthy and always wins, even during a
              // wiggle sweep or the grace period below.
              left && !right -> {
                  lastSide = -1
                  ticksLost = 0
                  baseSpeed + turnBoost to baseSpeed - turnBoost
              }
              right && !left -> {
                  lastSide = 1
                  ticksLost = 0
                  baseSpeed - turnBoost to baseSpeed + turnBoost
              }
              left && right -> {
                  ticksLost = 0
                  baseSpeed to baseSpeed
              }
              center -> {
                  ticksLost = 0
                  wiggle()
              }
              else -> {
                  ticksLost++
                  if (ticksLost <= gracePeriodTicks) {
                      // Brief gap in the line (e.g. a dash): keep weaving/advancing rather than
                      // committing to a full search curve over one or two missed ticks.
                      wiggle()
                  } else if (lastSide < 0) {
                      searchForward + searchTurn to searchForward - searchTurn
                  } else {
                      searchForward - searchTurn to searchForward + searchTurn
                  }
              }
          }
          robot.perform(MoveCommand(robot.actuator, l, r))
      }
   
      private fun wiggle(): Pair<Double, Double> {
          val phase = (tick / wigglePeriodTicks) % 2
          return if (phase == 0) baseSpeed - wiggleTurn to baseSpeed + wiggleTurn
          else baseSpeed + wiggleTurn to baseSpeed - wiggleTurn
      }
}