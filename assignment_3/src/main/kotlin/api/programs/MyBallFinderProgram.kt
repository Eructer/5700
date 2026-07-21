package api.programs
 
import api.RobotApi
import api.RobotProgram
import command.MoveCommand
import observer.Observer
import javafx.scene.paint.Color

class MyBallFinderProgram : RobotProgram {
    override val name: String
        get() = "My Ball Finder Program"

    private val chaseSpeed = 90.0
    private val scanForward = 40.0
    private val scanTurn = 25.0
    private val wigglePeriodTicks = 25

    private var tick = 0

    private fun isBallAhead(color: Color): Boolean =
        color.red > 0.6 && color.green < 0.4 && color.blue < 0.4

    private fun scan(): Pair<Double, Double> {
        val phase = (tick / wigglePeriodTicks) % 2
        return if (phase == 0) scanForward - scanTurn to scanForward + scanTurn
        else scanForward + scanTurn to scanForward - scanTurn
    }

    private lateinit var robot: RobotApi
 
    private val visionSub: Observer<Color> = Observer {value -> 
        tick++
        val (left, right) = when {
            isBallAhead(value) -> {
                chaseSpeed to chaseSpeed
            }

            else -> {
                scan()
            }
        }
        
        robot.perform(MoveCommand(robot.actuator, left, right))
    }

    private val sonarSub: Observer<Double> = Observer {distance ->
        if (distance <= 90.0) {
            robot.perform(MoveCommand(robot.actuator, -80.0, 80.0))
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
 
        robot.sensors.vision.subscribe(visionSub)
        robot.sensors.collision.subscribe(collisionSub)
        robot.sensors.sonar.subscribe(sonarSub)
    }
 
    override fun stopProgram(robot: RobotApi) {
        robot.sensors.vision.unsubscribe(visionSub)
        robot.sensors.collision.unsubscribe(collisionSub)
        robot.sensors.sonar.unsubscribe(sonarSub)
        robot.perform(MoveCommand(robot.actuator, 0.0, 0.0))
    }
}
 