package api.programs

import api.RobotApi
import api.RobotProgram
import observer.Observer

class MyHeatMapProgram: RobotProgram {
    override val name: String
        get() = "My Heat Map Program"

    private lateinit var temperatureSub: Observer<Double>
    private lateinit var collisionSub: Observer<Boolean>

    override fun startProgram(robot: RobotApi) {

        temperatureSub = TemperatureObserver(robot)

        robot.sensors.temperature.subscribe(temperatureSub)
        
    }

    override fun stopProgram(robot: RobotApi) {
        robot.sensors.temperature.unsubscribe(temperatureSub)
    }
}