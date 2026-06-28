package com.shapes

import kotlin.math.sqrt
import kotlin.math.pow
import com.shapes.Point

class Line(private val point1: Point, private val point2: Point) {
    private val xComponent: Double = (this.point2.getX() - this.point1.getX()).pow(2.0)
    
    private val yComponent: Double = (this.point2.getY() - this.point1.getY()).pow(2.0)
    
    private val length = if (sqrt(xComponent + yComponent) != 0.0) {
        sqrt(xComponent + yComponent)
    } else {
        throw Exception("Length is 0 or below")
    }

    fun getPoints() = Pair(this.point1, this.point2)

    fun getSlope(): Double{
        val slope: Double = (this.point2.getY() - this.point1.getY())/(this.point2.getX() - this.point1.getX())
        return slope
    }

    fun getLength() = this.length

    fun move(delta1: Pair<Double, Double>) {
        this.point1.move(delta1)
        this.point2.move(delta1)
    }
}
