class Point(val x: Int, val y: Int)    

def concat(list1: List[Point], list2: List[Point]): List[Point] = list1 match {
    case Nil => list2
    case head :: rest => head :: concat(rest, list2)
}  

def mapc(list: List[Point], func: Point => Point, cont: List[Point] => List[Point]): List[Point] = list match {
    case Nil => cont(Nil)
    case head :: rest => mapc(rest, func, x => cont( func(head) :: x))
}

def lineR(x1: Int, y1: Int, x2: Int, y2: Int, dx: Int, dy: Int, d: Int, list: List[Point]): List[Point] = {
    
    if(x1 == x2) {        
        return new Point(x1, y2) :: list
    } else {      
        val myList = new Point(x1, y1) :: list
                
        if(d > 0) {
            lineR(x1 + 1, y1 + 1, x2, y2, dx, dy, d - 2*dx + 2*dy, myList)
        } else {
            lineR(x1 + 1, y1, x2, y2, dx, dy, d + 2*dy, myList)
        }     
    }
}

def lineStart(x1in: Int, y1in: Int, x2in: Int, y2in: Int): List[Point] = {
        
    // place line at 0,0    
    val x1 = 0
    val x2 = x2in - x1in
    val y1 = 0
    val y2 = y2in - y1in

    // find out what octant
    if(y2 > x2) { // bad octant
               
        val d = 2*x2 - y2

        // create list as if it was in good octant
        val list = lineR(x1, y1, y2, x2, y2, x2, d, List[Point]())

        // flip line to bad octant
        val flipList = mapc(list, point => new Point(point.y, point.x), x => x)

        // place back at x1,y2
        return mapc(flipList, point => new Point(point.x + x1in, point.y + y1in), x => x)
        
    } else { // good octant
        
        val d = 2*y2 - x2

        // create list in good octant
        val list = lineR(x1, y1, x2, y2, x2, y2, d, List[Point]())

        // place back at x1, y2
        return mapc(list, point => new Point(point.x + x1in, point.y + y1in), x => x)
    } 
}

def rectangle(x1: Int, y1: Int, x2: Int, y2: Int): List[Point] = {
    val bottom = lineStart(x1, y1, x2, y1) // Bottom line
    val right = lineStart(x2, y1, x2, y2) // Right line
    val top = lineStart(x1, y2, x2, y2) // Top line
    val left = lineStart(x1, y1, x1, y2) // Left line

    return concat(concat(bottom, right), concat(top, left))

    // println("Bottom")
    // print(bottom)
    
    // println("Right")
    // print(right)
    

    // println("Top")
    // print(top)
    
    // println("Left")
    // print(left)
}

def print(list: List[Point]) {
    for (point <- list) {
        println("x:" + point.x + " y:" + point.y)
    }
}

// Good site
val line1 = lineStart(1,1, 5,1) // horizontal
val line2 = lineStart(1,1, 5,5) // diagonal

// Bad site
val line3 = lineStart(1,1, 1,5) // vertical
val line4 = lineStart(5,1, 5,5) // vertical

// cross
val line5 = lineStart(3,4, 4,8)

// println("Good horizontal")
// print(line1)

// println("Good diagonal")
// print(line2)

// println("Bad vertical at 1,1")
// print(line3)

// println("Bad vertical at 5,1")
// print(line4)

// println("Cross")
// print(line5)

val rectangle1 = rectangle(1,1, 5,5)
print(rectangle1)