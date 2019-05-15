package sample;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import scala.Tuple4;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class Controller {
    public TextArea textOutput;
    public TextArea textInput;
    public Canvas canvas;
    public boolean timerTaskRunning = false;
    Timer timer=new Timer();
    List L;

    int i = 1;
    Color circleColor = Color.web("#0000FF");
    Color yellowColor = Color.web("#FF0000");
    BitmapOps bitmapOps = new BitmapOps(this);
    TextparserJava parser = new TextparserJava(bitmapOps);
    //private TextParser parser = TextParser.textParser(bitmapOps,null);
    //private TextParser parser = new TextParser(bitmapOps)

    Runnable runTask = new Runnable() {
        @Override
        public void run() {
            //double width = canvas.getWidth();
            //double height = canvas.getHeight();
            //canvas.getGraphicsContext2D().clearRect(0,0,width,height);
            drawOnCanvas(L);
            timerTaskRunning=false;
        }
    };


    class Point{
        int x;
        int y;
        Color c;
    }

    public void inputChanged(KeyEvent keyEvent) {
        String hello = textInput.getText();
        System.out.println(hello);
        //timer = new Timer();

        TimerTask task = new TimerTask()
        {
            public void run()
            {
                //double width = canvas.getWidth();
                //double height = canvas.getHeight();
                //canvas.getGraphicsContext2D().clearRect(0,0,width,height);
                Platform.runLater(runTask);

            }

        };
        //TextParser.textParser(bitmapOps,null);
        try{
            L = parser.textParserJava(hello);
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            canvas.getGraphicsContext2D().clearRect(0,0,width,height);
            drawOnCanvasFirst(L);

        }catch(Exception e){
            System.out.println("error in input");
        }

        try{
            if(!timerTaskRunning) {
                timerTaskRunning=true;
                timer=new Timer();
                timer.schedule(task, 200l);
            }else{
                timer.cancel();
                timer = new Timer();
                timer.schedule(task, 200l);
            }
        }catch (Exception e){
            System.out.println(e);
        }

        //List L = (bitmapOps.startMidtpoint(200,200,10*i,circleColor));
        //List L2 =(bitmapOps.typeString(30*i,40*i,circleColor,"hello World"));


        //drawOnCanvas(L2,i);

        //i++;

    }
    public void drawOnCanvas(List L){
        for(Object e : L){
            Tuple4<Integer,Integer,Color,String> t = (Tuple4<Integer,Integer,Color,String>)e;
            if (t._4()==null) {
                canvas.getGraphicsContext2D().getPixelWriter().setColor(t._1(), t._2(), t._3());
            }else{
                canvas.getGraphicsContext2D().strokeText(t._4(),t._1(),t._2());
            }
        }
    }
    public void drawOnCanvasFirst(List L){
        if(!L.isEmpty())
            textOutput.clear();
        for(Object e : L){
            Tuple4<Integer,Integer,Color,String> t = (Tuple4<Integer,Integer,Color,String>)e;
            if (t._4()==null) {
                canvas.getGraphicsContext2D().getPixelWriter().setColor(t._1(), t._2(), yellowColor);
            }else{
                canvas.getGraphicsContext2D().strokeText(t._4(),t._1(),t._2());
            }
        }
    }
    public void showError(String msg){
        textOutput.setText(msg);
    }

}
