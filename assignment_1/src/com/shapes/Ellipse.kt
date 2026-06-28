package com.shapes
import com.shapes.Point
import kotlin.math.PI

open class Ellipse(private val point: Point, private val radius1: Double, private val radius2: Double): Move {
    init {
        require(radius1 > 0.0 && radius2 > 0.0) {"Radii must both be positive"}
    }
    fun getPoint() = this.point.clone()

    fun getRadii(): Pair<Double, Double> {
        return Pair(this.radius1, this.radius2)
    }

    //pi * a * b. Radius 1 is for center along the x axis, radius 2 is for center along y axis
    fun getArea() = if ((PI * this.radius1 * this.radius2) > 0) {PI * this.radius1 * this.radius2} else {throw Exception("Ellipse area is 0")}

    override fun move(delta: Pair<Double, Double>) {
        this.point.move(delta)
    }
}