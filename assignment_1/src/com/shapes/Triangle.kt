package com.shapes

import kotlin.math.sqrt
import kotlin.math.pow
import com.shapes.Point

class Triangle(private val point1: Point, private val point2: Point, private val point3: Point) {
    private val side1: Double
    private val side2: Double
    private val side3: Double
    
    init {
        this.side1 = updateSides(this.point1, this.point2)

        this.side2 = updateSides(this.point2, this.point3)

        this.side3 = updateSides(this.point3, this.point1)
        // Check if inputs are valid by getting the area
        getArea()
    }
    
    private fun updateSides(point1: Point, point2: Point): Double {
        val xComponent: Double = (point2.getX() - point1.getX()).pow(2.0)
        val yComponent: Double = (point2.getY() - point1.getY()).pow(2.0)
        return sqrt(xComponent + yComponent)
    }
    
    fun getPoints() = Triple(this.point1, this.point2, this.point3)

    // Using Heron's formula
    fun getArea(): Double {
        // Check that the points don't line on the same line
        if (side1 + side2 < side3 && side2 + side3 < side1 && side1 + side3 < side2) {
            throw Exception("Points don't form a triangle")
        }
        
        val semiPerimiter = (this.side1+this.side2+this.side3)/2.0

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