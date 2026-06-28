package com.shapes

import kotlin.math.abs
import com.shapes.Point

class Square(private val point1: Point, private val point2: Point) {

    private val width = if (abs(this.point1.getX() - this.point2.getX()) == abs(this.point1.getY() - this.point2.getY())) {
        abs(this.point1.getX() - this.point2.getX())
    } else {
        throw Exception("Invalid width, must be same as height")
    }

    private val height = if (abs(this.point1.getX() - this.point2.getX()) == abs(this.point1.getY() - this.point2.getY())) {
        abs(this.point1.getY() - this.point2.getY())
    } else {
        throw Exception("Invalid height, must be same as width")
    }

    fun getPoints() = Pair(this.point1, this.point2)

    fun getArea(): Double {
        val area: Double = (abs(this.point2.getX() - this.point1.getX()) * abs(this.point2.getY() - this.point1.getY()))
        return area
    }

    fun move(delta1: Pair<Double, Double>) {
        this.point1.move(delta1)
        this.point2.move(delta1)
    }
}
