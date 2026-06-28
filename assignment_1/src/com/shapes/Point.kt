package com.shapes

class Point(private var x: Double, private var y: Double): Move {
    fun getX() = this.x

    fun getY() = this.y

    override fun move(delta: Pair<Double, Double>) {
        this.x += delta.first
        this.y += delta.second
    }

    fun clone(): Point {
        return Point(this.x, this.y)
    }
}
