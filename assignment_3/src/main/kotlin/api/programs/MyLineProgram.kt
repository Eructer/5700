package api.programs

import api.RobotApi
import api.RobotProgram
import command.MoveCommand
import observer.Observer

class MyLineFollowerProgram : RobotProgram {
    override val name: String
        get() = "My Line Follower Program"

    private lateinit var robot: RobotApi

    private val leftSub: Observer<Boolean> = Observer { seesLine ->
        if (!seesLine) {
            robot.perform(
                MoveCommand(robot.actuator, -40.0, 40.0)
            )
        }
    }
    private val centerSub: Observer<Boolean> = Observer { seesLine ->
        if (seesLine) {
            robot.perform(
                MoveCommand(robot.actuator, 60.0, 60.0)
            )
        }
    }
    private val rightSub: Observer<Boolean> = Observer { seesLine ->
        if (!seesLine) {
            robot.perform(
                MoveCommand(robot.actuator, 40.0, -40.0)
            )
        }
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

        robot.sensors.lineLeft.subscribe(leftSub)
        robot.sensors.lineCenter.subscribe(centerSub)
        robot.sensors.lineRight.subscribe(rightSub)
        robot.sensors.collision.subscribe(collisionSub)
    }

    override fun stopProgram(robot: RobotApi) {
        robot.sensors.lineLeft.unsubscribe(leftSub)
        robot.sensors.lineCenter.unsubscribe(centerSub)
        robot.sensors.lineRight.unsubscribe(rightSub)
        robot.sensors.collision.unsubscribe(collisionSub)
        robot.perform(MoveCommand(robot.actuator, 0.0, 0.0))
    }
}
c