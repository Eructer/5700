package com.shapes

import kotlin.math.sqrt
import kotlin.math.pow
import com.shapes.Point

class Line(private val point1: Point, private val point2: Point) {
    fun getPoints() = Pair(this.point1.clone(), this.point2.clone())

    fun getSlope(): Double{
        val dx = point2.getX() - point1.getX()
        require(dx != 0.0) {"Undefined slope"}
        return (point2.getY() - point1.getY()) / dx
    }

    fun getLength(): Double {
        val dx = point2.getX() - point1.getX()
        val dy = point2.getY() - point1.getY()
        val length = sqrt(dx * dx + dy * dy)
        if (length == 0.0) {
            throw Exception("Invalid length")
        }
        return sqrt(dx * dx + dy * dy)
    }

    fun move(delta1: Pair<Double, Double>) {
        this.point1.move(delta1)
        this.point2.move(delta1)
    }
}
