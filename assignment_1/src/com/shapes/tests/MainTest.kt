package com.shapes.tests
import com.shapes.*

fun testPoint() {
    val p = Point(3.0, 4.0)

    val x = p.getX()

    val y = p.getY()

    check(x == 3.0) {"Error: X value not the same"}
    check(y == 4.0) {"Error: Y value not the same"}

    val c = p.clone()
    val cX = c.getX()
    val cY = c.getY()
    check(c !== p) {"Error: Clone not the same as original"}
    check(cX == x && cY == y) {"Error: Clone X and Y not equal to original X and Y"}

    val delta: Pair<Double, Double> = Pair(2.0, 2.0)
    p.move(delta)
    val newX = p.getX()
    val newY = p.getY()
    check(newX == 5.0) {"Error: Move X not correct"}
    check(newY == 6.0) {"Error: Move Y not correct"}
}

fun testLine() {
    val a = Point(0.0, 0.0)
    val b = Point(3.0, 4.0)

    val line = Line(a, b)

    val lineLength = line.getLength()

    check(lineLength == 5.0) {"Error: Not expected line length $lineLength"}
    check(line.getSlope() == 4.0 / 3.0) {"Error: Not expected line slope"}

    val delta: Pair<Double, Double> = Pair(1.0, 1.0)
    line.move(delta)
    val points = line.getPoints()
    check(points.first.getX() == 1.0 && points.first.getY() == 1.0) {"Error: Line x axis move"}
    check(points.second.getX() == 4.0 && points.second.getY() == 5.0) {"Error: Line y axis move"}
}

fun testInvalidLine() {
    var failed = false

    try{
        Line(Point(1.0, 1.0), Point(1.0, 1.0))
    } catch (e: Exception) {
        failed = true
    }

    check(failed)
}

fun testRectangle() {
    val r = Rectangle(Point(0.0, 0.0), Point(4.0, 3.0))

    check(r.getArea() == 12.0)

    val delta: Pair<Double, Double> = Pair(1.0, 2.0)
    r.move(delta)

    val points = r.getPoints()
    check(points.first.getX() == 1.0 && points.first.getY() == 2.0)
    check(points.second.getX() == 5.0 && points.second.getY() == 5.0)
}

fun testInvalidRectangle() {
    var failed = false

    try {
        Rectangle(Point(0.0, 0.0), Point(0.0, 5.0))
    } catch (e: Exception) {
        failed = true
    }

    check(failed)
}

fun testSquare() {
    val s = Square(Point(0.0, 0.0), Point(3.0, 3.0))

    check(s.getArea() == 9.0)

    var failed = false

    try {
        Square(Point(0.0,0.0), Point(3.0, 4.0))
    } catch (e: Exception) {
        failed = true
    }

    check(failed)
}

fun testEllipse() {
    val e = Ellipse(Point(0.0, 0.0), 3.0, 2.0)

    check(e.getArea() == Math.PI * 6.0)

    val delta: Pair<Double, Double> = Pair(1.0, 2.0)
    e.move(delta)
    check(e.getPoint().getX() == 1.0)
    check(e.getPoint().getY() == 2.0)
}

fun testInvalidEllipse() {
    var failed = false

    try {
        Ellipse(Point(0.0, 0.0), 0.0, 2.0)
    } catch (e: Exception) {
        failed = true
    }

    check(failed)
}

fun testCircle() {
    val c = Circle(Point(0.0, 0.0), 2.0)

    check(c.getArea() == Math.PI * 4.0)
    check(c.getRadius() == 2.0)
}

fun testTriangle() {
    val t = Triangle(
        Point(0.0, 0.0),
        Point(4.0, 0.0),
        Point(0.0, 3.0)
    )

    val area = t.getArea()
    
    check(area == 6.0) {"Error: Area not expected, $area != 6.0"}

    val delta: Pair<Double, Double> = Pair(1.0, 1.0)
    t.move(delta)

    val pts = t.getPoints()

    check(pts.first.getX() == 1.0 && pts.second.getX() == 5.0 && pts.third.getX() == 1.0) {"Error: Move points unexpected"}
}

fun testInvalidTriangle() {
    var failed = false

    try {
        val t = Triangle(
            Point(0.0, 0.0),
            Point(1.0, 1.0),
            Point(2.0, 2.0)
        )
    } catch (e: Exception) {
        failed = true
    }

    check(failed)
}

fun runTest(name: String, block: () -> Unit) {
    try {
        println("--- Testing $name")
        block()
        println("Success $name")
    } catch (e: Exception) {
        println("Failure -> ${e.message}")
    }
}

fun allTests() {
    // Test point
    runTest("Point") { testPoint() }
    // Test line
    runTest("Line") { testLine() }
    // Test invalid line
    runTest("Invalid Line") { testInvalidLine() }
    // Test rectangle
    runTest("Rectangle") { testRectangle() }
    // Test invalid rectangle
    runTest("Invalid Rectangle") { testInvalidRectangle() }
    // Test square
    runTest("Square") { testSquare() }
    // Test ellipse
    runTest("Ellipse") { testEllipse() }
    // Test invalid ellipse
    runTest("Invalid ellipse") { testInvalidEllipse() }
    // Test Circle
    runTest("Circle") { testCircle() }
    // Test triangle
    runTest("Triangle") { testTriangle() }
    // Test invalid triangle
    runTest("Invalid triangle") { testInvalidTriangle() }
}