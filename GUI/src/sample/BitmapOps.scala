package sample

import javafx.scene.paint.Color
import scala.collection.JavaConverters._
class BitmapOps(con:Controller) {

  def startMidtpoint(x0:Int, y0:Int, radius:Int, c:Color):java.util.List[(Int, Int, Color,String)]={
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

    return circleBody(L4,x0,y0,x,y,ddF_x,ddF_y,f,c).asJava // enables java to understand the format
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
      con.showError("ERROR During blah")// is this resonable
      return L12
    }

    if(f >= 0)
    {
      circleBody(L12,x0,y0,x+1,y-1,ddF_x+2,ddF_y+2,f+((ddF_x+2)+(ddF_y+2)),c)
    }else{
      circleBody(L12,x0,y0,x+1,y,ddF_x+2,ddF_y,f+ddF_x,c)
    }
  }
  def typeString(x:Int,y:Int,c:Color,s:String):java.util.List[(Int, Int, Color,String)] ={
    val L = List[(Int,Int,Color,String)]()
    return makeList(L,x,y,c,s).asJava
  }

  def printList(list:List[(Int,Int,Color,String)])={
    list.foreach{
      case (x,y,c,s)=>println(s"Point $x,$y with color $c string: $s")
      case _ => println("Done")
    }
  }
  def fill(list:List[(Int,Int,Color,String)])={

  }

  def makeList(list:List[(Int,Int,Color,String)],x:Int,y:Int, c:Color,s:String):List[(Int,Int,Color,String)]={
    return list:+(x,y,c,s)
  }


}