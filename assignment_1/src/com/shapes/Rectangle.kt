package com.shapes

import kotlin.math.abs
import com.shapes.Point

open class Rectangle(point1: Point, point2: Point): Move {
    private var width: Double
    private var height: Double
    private val point1: Point
    private val point2: Point

    init {
        this.point1 = point1.clone()
        this.point2 = point2.clone()
        width = abs(this.point1.getX() - this.point2.getX())
        height = abs(this.point1.getY() - this.point2.getY())
        require(width > 0.0) {"Invalid width"}
        require(height > 0.0) {"Invalid height"}
    }

    fun getPoints() = Pair(this.point1.clone(), this.point2.clone())

    fun getArea(): Double {
        val area: Double = (abs(this.point2.getX() - this.point1.getX()) * abs(this.point2.getY() - this.point1.getY()))
        return area
    }

    override fun move(delta: Pair<Double, Double>) {
        this.point1.move(delta)
        this.point2.move(delta)
    }
}
