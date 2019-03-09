class Point(xc: Int, yc: Int) {
    var x: Int = xc
    var y: Int = yc
}


class PointList
    case class Cons(x: Int, y: Int, tl: PointList) extends PointList
    case class Nil() extends PointList    

var list = Cons(1,1, Cons(1,2, Cons(1,3, Nil())));

def line(x1: Int, y1: Int, x2: Int, y2: Int) {
    var dx = x2 - x1
    var dy = y2 - y1
    var d = 2*dy - dx
    var y = y1

    for (x <- x1 to x2) {
        println("x:" + x + " y:" + y)
        if (d > 0) {
            y = y + 1
            d = d - 2*dx
        }
        d = d + 2*dy
    }
}

def lineStart(x1: Int, y1: Int, x2: Int, y2: Int) {
    var dx = x2 - x1
    var dy = y2 - y1
    var d = 2*dy - dx
    
    lineR(x1, y1, x2, y2, dx, dy, d)
}
def lineR(x1: Int, y1: Int, x2: Int, y2: Int, dx: Int, dy: Int, d: Int) {
    var newY = y1
    var newD = d
    
    if(x1 == x2) {
        println("x:" + x1 + " y:" + y2)
    } else {
        println("x:" + x1 + " y:" + newY)
        if (d > 0) {
            newY = y1 + 1
            newD = d - 2*dx
        }
        newD = newD + 2*dy

        lineR(x1 + 1, newY, x2, y2, dx, dy, newD)
    }
}
line(1,1, 15,3)
lineStart(1,1, 15,3)