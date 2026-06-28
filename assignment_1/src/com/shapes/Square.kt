package com.shapes

import kotlin.math.abs
import com.shapes.Point
import com.shapes.Rectangle

class Square(private val point1: Point, private val point2: Point): Rectangle(point1, point2) {
    init {
        require(abs(this.point1.getX() - this.point2.getX()) == abs(this.point1.getY() - this.point2.getY())) {"Square must have equal width and height"}
    }
}
