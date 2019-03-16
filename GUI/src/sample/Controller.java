package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import scala.Tuple4;
import java.util.List;


public class Controller {
    public TextArea textOutput;
    public TextArea textInput;
    public Canvas canvas;
    int i = 1;
    Color circleColor = Color.web("#0000FF");

    class Point{
        int x;
        int y;
        Color c;
    }

    public void inputChanged(KeyEvent keyEvent) {
        String hello = keyEvent.getText();

        BitmapOps bitmapOps = new BitmapOps(this);
        List L = (bitmapOps.startMidtpoint(200,200,10*i,circleColor));
        List L2 =(bitmapOps.typeString(30*i,40*i,circleColor,"hello World"));

        drawOnCanvas(L,i);
        drawOnCanvas(L2,i);

        i++;

    }
    public void drawOnCanvas(List L,int i){
        for(Object e : L){
            Tuple4<Integer,Integer,Color,String> t = (Tuple4<Integer,Integer,Color,String>)e;
            if (t._4()==null) {
                canvas.getGraphicsContext2D().getPixelWriter().setColor(t._1(), t._2(), t._3());
            }else{
                canvas.getGraphicsContext2D().strokeText(t._4(),30*i,40*i);
            }
        }
    }
    public void showError(String msg){
        textOutput.setText(msg);
    }

}
