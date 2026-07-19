package api.programs

import api.RobotApi
import command.MoveCommand
import observer.Observer
import kotlin.random.Random

class TemperatureObserver(private val robot: RobotApi): Observer<Double> {

    private val windowTicks = 20
    private val minDelta = 0.15
    private val patienceWindows = 20
    
    private val forwardSpeed = 50.0
    private val tumbleForward = 35.0
    private val tumbleTurn = 55.0
    private val tumbleTicks = 20
    
    private var referenceValue = Double.NEGATIVE_INFINITY
    private var ticksInWindow = 0
    private var nonImprovingWindows = 0
    private var arrived = false
    
    private var tumbleTicksRemaining = 0
    private var tumbleLeft = 0.0
    private var tumbleRight = 0.0



    override fun onUpdate(value: Double) {
        if (arrived) {
            robot.perform(MoveCommand(robot.actuator, 0.0, 0.0))
            return
        }
        
        if (tumbleTicksRemaining > 0) {
            tumbleTicksRemaining--
            robot.perform(MoveCommand(robot.actuator, tumbleLeft, tumbleRight))
            return
        }
    
        ticksInWindow++
        if (ticksInWindow < windowTicks) {
            robot.perform(MoveCommand(robot.actuator, forwardSpeed, forwardSpeed))
            return
        }
    
        val improved = value > referenceValue + minDelta
        referenceValue = value
        ticksInWindow = 0
    
        if (improved) {
            nonImprovingWindows = 0
            robot.perform(MoveCommand(robot.actuator, forwardSpeed, forwardSpeed))
            return
        }
        nonImprovingWindows++
        if (nonImprovingWindows >= patienceWindows) {
            arrived = true
            return
        }
    
        tumbleTicksRemaining = tumbleTicks
        if (Random.nextBoolean()) {
            tumbleLeft = tumbleForward - tumbleTurn
            tumbleRight = tumbleForward + tumbleTurn
        } else {
            tumbleLeft = tumbleForward + tumbleTurn
            tumbleRight = tumbleForward - tumbleTurn
        }
        robot.perform(MoveCommand(robot.actuator, tumbleLeft, tumbleRight))
    }
}