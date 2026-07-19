package api.programs

import api.RobotApi
import api.RobotProgram
import command.MoveCommand
import observer.Observer

class MyLineFollowerProgram : RobotProgram {
    override val name: String
        get() = "My Line Follower Program"

    private lateinit var lineSub: Observer<Boolean>
    private lateinit var collisionSub: Observer<Boolean>

    override fun startProgram(robot: RobotApi) {
        lineSub = LineObserver(robot)
        collisionSub = CollisionObserver(robot)

        robot.sensors.lineCenter.subscribe(lineSub)
        robot.sensors.collision.subscribe(collisionSub)
    }

    override fun stopProgram(robot: RobotApi) {
        robot.sensors.lineCenter.unsubscribe(lineSub)
        robot.sensors.collision.unsubscribe(collisionSub)
        robot.perform(MoveCommand(robot.actuator, 0.0, 0.0))
    }
}
