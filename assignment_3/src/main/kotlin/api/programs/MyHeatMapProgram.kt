package api.programs

import api.RobotApi
import api.RobotProgram
import observer.Observer
import command.MoveCommand
import kotlin.random.Random

class MyHeatMapProgram: RobotProgram {
    override val name: String
        get() = "My Heat Map Program"

    private lateinit var robot: RobotApi

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
        
    private val temperatureSub: Observer<Double> = Observer { value ->
        if (arrived) {
            robot.perform(MoveCommand(robot.actuator, 0.0, 0.0))
            return@Observer
        }
    
        if (tumbleTicksRemaining > 0) {
            tumbleTicksRemaining--
            robot.perform(MoveCommand(robot.actuator, tumbleLeft, tumbleRight))
            return@Observer
        }
    
        ticksInWindow++
        if (ticksInWindow < windowTicks) {
            robot.perform(MoveCommand(robot.actuator, forwardSpeed, forwardSpeed))
            return@Observer
        }
    
        val improved = value > referenceValue + minDelta
        referenceValue = value
        ticksInWindow = 0
    
        if (improved) {
            nonImprovingWindows = 0
            robot.perform(MoveCommand(robot.actuator, forwardSpeed, forwardSpeed))
            return@Observer
        }
    
        nonImprovingWindows++
    
        if (nonImprovingWindows >= patienceWindows) {
            arrived = true
            robot.perform(MoveCommand(robot.actuator, 0.0, 0.0))
            return@Observer
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

    
    private val collisionSub: Observer<Boolean> = Observer { collision ->
        if (collision) {
            robot.perform(
                MoveCommand(robot.actuator, 0.0, 60.0)
            )
        }
    }

    override fun startProgram(robot: RobotApi) {
        this.robot = robot
        robot.sensors.temperature.subscribe(temperatureSub)
        robot.sensors.collision.subscribe(collisionSub)
        
    }

    override fun stopProgram(robot: RobotApi) {
        robot.sensors.temperature.unsubscribe(temperatureSub)
        robot.sensors.collision.unsubscribe(collisionSub)
        robot.perform(MoveCommand(robot.actuator, 0.0, 0.0))
        arrived = false
    }
}