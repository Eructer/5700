package com.shapes

import kotlin.math.pow
import kotlin.math.PI
import com.shapes.Point

class Circle(private val point: Point, private val radius1: Double) {
    private val radius = radius1
    
    fun getPoint() = point

    fun getRadius(): Double {
        return this.radius1
    }

    fun getArea() = if ((PI * this.radius.pow(2)) > 0) {PI * this.radius.pow(2)} else {throw Exception("Circle area is 0")}

    fun move(delta: Pair<Double, Double>) {
        this.point.move(delta)
    }
}