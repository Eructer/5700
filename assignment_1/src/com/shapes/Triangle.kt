package com.shapes

import kotlin.math.sqrt
import kotlin.math.pow
import com.shapes.Point

class Triangle(private val point1: Point, private val point2: Point, private val point3: Point) {    
    private fun lineFromPoint(point1: Point, point2: Point): Double {
        val xComponent: Double = (point2.getX() - point1.getX()).pow(2.0)
        val yComponent: Double = (point2.getY() - point1.getY()).pow(2.0)
        return sqrt(xComponent + yComponent)
    }
    
    fun getPoints() = Triple(this.point1.clone(), this.point2.clone(), this.point3.clone())

    // Using Heron's formula
    fun getArea(): Double {
        val side1 = lineFromPoint(point1, point2)
        val side2 = lineFromPoint(point2, point3)
        val side3 = lineFromPoint(point3, point1)
        // Check that the points don't line on the same line
        if (side1 + side2 <= side3 && side2 + side3 <= side1 && side1 + side3 <= side2) {
            throw Exception("Points don't form a triangle")
        }
    
        val semiPerimiter = (side1 + side2 + side3)/2.0

        val area: Double = sqrt(semiPerimiter*(semiPerimiter - side1)*(semiPerimiter - side2)*(semiPerimiter - side3))

        if (area == 0.0) {
            throw Exception("Triangle area is 0")
        } else {
            return area
        }
    }

    fun move(delta: Pair<Double, Double>) {
        this.point1.move(delta)
        this.point2.move(delta)
        this.point3.move(delta)
    }
}