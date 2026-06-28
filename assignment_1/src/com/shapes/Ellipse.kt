package com.shapes
import com.shapes.Point
import kotlin.math.PI

class Ellipse(private val point: Point, private val radius1: Double, private val radius2: Double) {
    private val area = getArea()

    fun getPoint() = point

    fun getRadii(): Pair<Double, Double> {
        return Pair(this.radius1, this.radius2)
    }

    //pi * a * b. Radius 1 is for center along the x axis, radius 2 is for center along y axis
    fun getArea() = if ((PI * this.radius1 * this.radius2) > 0) {PI * this.radius1 * this.radius2} else {throw Exception("Ellipse area is 0")}

    fun move(delta: Pair<Double, Double>) {
        this.point.move(delta)
    }
}