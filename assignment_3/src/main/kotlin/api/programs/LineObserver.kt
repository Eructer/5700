package api.programs

import api.RobotApi
import command.MoveCommand
import observer.Observer


class LineObserver(private val robot: RobotApi): Observer<Boolean> {

    private val baseSpeed = 60.0
    private val turnBoost = 45.0
    private val searchSpin = 30.0
    
    override fun onUpdate(value: Boolean) {
        val left = robot.sensors.lineLeft.reading ?: false
        val center = robot.sensors.lineCenter.reading ?: false
        val right = robot.sensors.lineRight.reading ?: false

        println("Left:${left}, Center:${center}, Right:${right} ")

        if (!value){
            val (l, r) = when {
                center -> baseSpeed to baseSpeed
                left && !right -> baseSpeed - turnBoost to baseSpeed + turnBoost
                right && !left -> baseSpeed + turnBoost to baseSpeed - turnBoost
                left && right -> baseSpeed to baseSpeed
                // Line lost entirely: spin slowly in place to search for it again.
                else -> -searchSpin to searchSpin
            }
        }


        robot.perform(MoveCommand(robot.actuator, l, r))
    }
}