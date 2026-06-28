package com.shapes

import kotlin.math.pow
import kotlin.math.PI
import com.shapes.Point

class Circle(private val point: Point, private val radius1: Double): Ellipse(point.clone(), radius1, radius1) {
    fun getRadius(): Double = getRadii().first
}