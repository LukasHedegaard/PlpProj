package sample

import javafx.scene.paint.Color
import sample.TextParser.CommandTransformer

import scala.collection.JavaConverters._
import TextParser.textParser


class TextparserJava(bitmapOps: BitmapOps){
  def textParserJava(in:String):java.util.List[(Int, Int, Color,String)]={
    val textParserjava = textParser(bitmapOps, println)
    val L_fail = List[(Int,Int,Color,String)]()
    var d = List[(Int,Int,Color,String)]()
    val L = textParserjava(in).getOrElse(L_fail)
    //val L = option.toList.flatten ++ L_fail
    return L.asJava

    //return bitmapOps.circle(200, 100, 50).asJava
  }
}

class BitmapOps(con:Controller) extends CommandTransformer[List[(Int, Int, Color,String)]]{


  def combine(p: List[(Int, Int, Color,String)], q: List[(Int, Int, Color,String)]): List[(Int, Int, Color,String)] = {
    return List[(Int,Int,Color,String)]()
  }
  def line(x1: Int, y1: Int, x2:Int, y2:Int): List[(Int, Int, Color,String)] = {
    return List[(Int,Int,Color,String)]()
  }
  def rectangle(x1: Int, y1: Int, x2: Int, y2: Int): List[(Int, Int, Color,String)] = {
    return List[(Int,Int,Color,String)]()
  }
  def circle(x: Int, y: Int, r: Int): List[(Int, Int, Color,String)] = {

    return startMidtpoint(x,y,r,Color.web("#0000FF"))
  }
  def textAt(x: Int, y: Int, t: String): List[(Int, Int, Color,String)] = {
    return List[(Int,Int,Color,String)]()
  }
  def boundingBox(x1: Int, y1: Int, x2: Int, y2: Int): List[(Int, Int, Color,String)] = {
    return List[(Int,Int,Color,String)]()
  }
  def boundingBox(x1: Int, y1: Int, x2: Int, y2: Int, rest: List[(Int, Int, Color,String)]): List[(Int, Int, Color,String)] = {
    return startMidtpoint(x1,y1,x2,Color.web("#0000FF"))
  }
  def draw(c: String, rest: List[(Int, Int, Color,String)]): List[(Int, Int, Color,String)] = {
    return List[(Int,Int,Color,String)]()
  }
  def fill(c: String, g: List[(Int, Int, Color,String)]): List[(Int, Int, Color,String)] = {
    return List[(Int,Int,Color,String)]()
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


}