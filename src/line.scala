class Point(val x: Int, val y: Int)    

def flip(point: Point): Point = {
    return new Point(point.y, point.x)
}

def mapc(list: List[Point], func: Point => Point, cont: List[Point] => List[Point]): List[Point] = list match {
    case Nil => cont(Nil)
    case s :: rest => mapc(rest, func, x => cont( func(s) :: x))
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

def lineStart(x1: Int, y1: Int, x2: Int, y2: Int): List[Point] = {
     
    if(y2 > x2) {
        val dx = y2 - x1
        val dy = x2 - y1
        val d = 2*dy - dx

        val list = lineR(x1, y1, y2, x2, dx, dy, d, List[Point]())
        return mapc(list, flip, x => x)
    } else {
        val dx = x2 - x1
        val dy = y2 - y1
        val d = 2*dy - dx

        return lineR(x1, y1, x2, y2, dx, dy, d, List[Point]())
    } 
}

def print(list: List[Point]) {
    for (point <- list) {
        println("x:" + point.x + " y:" + point.y)
    }
}

val line1 = lineStart(1, 1, 15, 3)
val line2 = lineStart(1, 1, 3, 15)

print(line1)
print(line2)