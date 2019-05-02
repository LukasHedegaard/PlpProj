package sample

import javafx.scene.paint.Color
import sample.TextParser.CommandTransformer

import scala.collection.JavaConverters._
import TextParser.textParser


class TextparserJava(bitmapOps: BitmapOps){
  def textParserJava(in:String):java.util.List[(Int, Int, Color,String)]={
    val textParserjava = textParser(bitmapOps, bitmapOps.controler.showError)
    val L_fail = List[(Int,Int,Color,String)]()
    var d = List[(Int,Int,Color,String)]()
    val L = textParserjava(in).getOrElse(L_fail)
    //val L = option.toList.flatten ++ L_fail
    return L.asJava

    //return bitmapOps.circle(200, 100, 50).asJava
  }
}

class BitmapOps(con:Controller) extends CommandTransformer[List[(Int, Int, Color,String)]]{
  val stdColor = Color.web("#0000FF")
  val controler = con;

  def combine(p: List[(Int, Int, Color,String)], q: List[(Int, Int, Color,String)]): List[(Int, Int, Color,String)] = {
    return p:::q
  }
  def line(x1: Int, y1: Int, x2:Int, y2:Int): List[(Int, Int, Color,String)] = {
    val pointList = lineStart(x1, y1, x2, y2)
    return f(pointList,List[(Int, Int, Color,String)]())
  }
  def rectangle(x1: Int, y1: Int, x2: Int, y2: Int): List[(Int, Int, Color,String)] = {
    val recList = rectangledraw(x1, y1, x2, y2)
    return f(recList,List[(Int, Int, Color,String)]())
  }
  def circle(x: Int, y: Int, r: Int): List[(Int, Int, Color,String)] = {

    return startMidtpoint(x,y,r,stdColor)
  }
  def textAt(x: Int, y: Int, t: String): List[(Int, Int, Color,String)] = {
    return typeString(x,y,null,t)
  }
  def boundingBox(x1: Int, y1: Int, x2: Int, y2: Int): List[(Int, Int, Color,String)] = {
    return List[(Int,Int,Color,String)]()
  }
  def boundingBox(x1: Int, y1: Int, x2: Int, y2: Int, rest: List[(Int, Int, Color,String)]): List[(Int, Int, Color,String)] = {
    return rest.filter(coor=>coor._1>=x1
                    && coor._1 <= x2
                    && coor._2 >= y1
                    && coor._2 <= y2)
  }
  def draw(c: String, rest: List[(Int, Int, Color,String)]): List[(Int, Int, Color,String)] = {
    return List[(Int,Int,Color,String)]()
  }
  def fill(c: String, g: List[(Int, Int, Color,String)]): List[(Int, Int, Color,String)] = {
    return List[(Int,Int,Color,String)]()
  }
  def f(list: List[Point], newList: List[(Int, Int, Color, String)]): List[(Int, Int, Color, String)] = list match{
    case Nil => return newList
    case head :: rest => f(rest, (head.x, head.y, stdColor, null) :: newList)
  }

  def startMidtpoint(x0:Int, y0:Int, radius:Int, c:Color):List[(Int, Int, Color,String)]={
    val f=1-radius
    val ddF_x=1
    val ddF_y= -2*radius
    val x=0
    val y=radius
    val s = null
    val L = List[(Int,Int,Color,String)]()
    val L1 = makeList(L,x0,y0+radius,c,s)//printPoint(x0, y0+radius,c)
    val L2 = makeList(L1,x0,y0-radius,c,s)//printPoint(x0, y0-radius,c)
    val L3 = makeList(L2,x0+radius,y0,c,s)//printPoint(x0+radius, y0,c)
    val L4 = makeList(L3,x0-radius,y0,c,s)//printPoint(x0-radius, y0,c)

    return circleBody(L4,x0,y0,x,y,ddF_x,ddF_y,f,c)
  }

  def circleBody(list:List[(Int,Int,Color,String)],x0:Int, y0:Int, x:Int, y:Int, ddF_x:Int, ddF_y:Int, f:Int, c:Color):List[(Int, Int, Color,String)]={
    val s = null
    val L5 = makeList(list,x0+x, y0+y,c,s)//printPoint(x0+x, y0+y,c)
    val L6 = makeList(L5,x0-x, y0+y,c,s)//printPoint(x0-x, y0+y,c)
    val L7 = makeList(L6,x0+x, y0-y,c,s)//printPoint(x0+x, y0-y,c)
    val L8 = makeList(L7,x0-x, y0-y,c,s)//printPoint(x0-x, y0-y,c)
    val L9 = makeList(L8,x0+y, y0+x,c,s)//printPoint(x0+y, y0+x,c)
    val L10 = makeList(L9,x0-y, y0+x,c,s)//printPoint(x0-y, y0+x,c)
    val L11 = makeList(L10,x0+y, y0-x,c,s)//printPoint(x0+y, y0-x,c)
    val L12 = makeList(L11,x0-y, y0-x,c,s)//printPoint(x0-y, y0-x,c)
    if (x>=y) {
      //con.showError("ERROR During blah")// is this resonable
      //fill(L12)
      return L12
    }

    if(f >= 0)
    {
      circleBody(L12,x0,y0,x+1,y-1,ddF_x+2,ddF_y+2,f+((ddF_x+2)+(ddF_y+2)),c)
    }else{
      circleBody(L12,x0,y0,x+1,y,ddF_x+2,ddF_y,f+ddF_x,c)
    }
  }
  def typeString(x:Int,y:Int,c:Color,s:String):List[(Int, Int, Color,String)] ={
    val L = List[(Int,Int,Color,String)]()
    return makeList(L,x,y,c,s)
  }

  def printList(list:List[(Int,Int,Color,String)])={
    list.foreach{
      case (x,y,c,s)=>println(s"Point $x,$y with color $c string: $s")
      case _ => println("Done")
    }
  }
  def fill(list:List[(Int,Int,Color,String)])={
    var i=0
    for (elem <- list) {
      for (elem2 <- list){
        if (elem._2 == elem2._2 & elem._1!=elem2._1) {
          val x1 = elem._1
          val y1 = elem._2
          val x2 = elem2._1
          val y2 = elem2._2

          i=i+1
          println(s"drawing $i th line from $x1,$y2 to $x2,$y2")//draw line
          // return list from draw linne should be appended to the rest by makelist
        }

      }
    }
  }

  def makeList(list:List[(Int,Int,Color,String)],x:Int,y:Int, c:Color,s:String):List[(Int,Int,Color,String)]={
    return list:+(x,y,c,s)
  }

  // line
  class Point(val x: Int, val y: Int)

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
  def concat(list1: List[Point], list2: List[Point]): List[Point] = list1 match {
    case Nil => list2
    case head :: rest => head :: concat(rest, list2)
  }
  def rectangledraw(x1: Int, y1: Int, x2: Int, y2: Int): List[Point] = {
    val bottom = lineStart(x1, y1, x2, y1) // Bottom line
    val right = lineStart(x2, y1, x2, y2) // Right line
    val top = lineStart(x1, y2, x2, y2) // Top line
    val left = lineStart(x1, y1, x1, y2) // Left line

    return concat(concat(bottom, right), concat(top, left))

  }

}