package api.programs
 
import api.RobotApi
import api.RobotProgram
import command.MoveCommand
import observer.Observer
import javafx.scene.paint.Color

class MyBallFinderProgram : RobotProgram {
    override val name: String
        get() = "My Ball Finder Program"
 
    private lateinit var visionSub: Observer<Color>
    private lateinit var collisionSub: Observer<Boolean>
 
    override fun startProgram(robot: RobotApi) {
        visionSub = VisionObserver(robot)
        collisionSub = CollisionObserver(robot)
 
        robot.sensors.vision.subscribe(visionSub)
        robot.sensors.collision.subscribe(collisionSub)
    }
 
    override fun stopProgram(robot: RobotApi) {
        robot.sensors.vision.unsubscribe(visionSub)
        robot.sensors.collision.unsubscribe(collisionSub)
        robot.perform(MoveCommand(robot.actuator, 0.0, 0.0))
    }
}
 