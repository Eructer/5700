package command


class MoveCommand(private val actuator: RobotActuator, private val left: Double, private val right: Double) : Command {

    private var previousLeft: Double = 0.0
    
    private var previousRight: Double = 0.0

    override fun execute() {
        previousLeft = actuator.leftTrackVelocity
        
        previousRight = actuator.rightTrackVelocity
        
        actuator.setTrackVelocities(left, right)
    }

    override fun undo() {
        actuator.setTrackVelocities(-previousLeft, -previousRight)
    }
    
}