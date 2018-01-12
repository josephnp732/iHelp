package iHelp;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Bounce extends Applet implements Runnable

{
    Demo redDemo, Back;
    Image img, I;

        public void init()
        {
            redDemo=new Demo(300,150,30,45,2,4);
            Back=new Demo(1,1,1000,650,0,0);
            I=getImage(getCodeBase(),"Campus.png");
            img =getImage(getDocumentBase(),"pin.png");
            Thread t=new Thread(this);
            t.start();
        }

        public void paint(Graphics g)
        {
                setBackground(Color.white);
                g.drawImage(I,Back.x,Back.y,Back.p,Back.q,null);
                g.drawImage(img,redDemo.x,redDemo.y,redDemo.p,redDemo.q ,null);
            	g.drawString("You are here", 288,210);
        }
 
        public void run()
        {

        while(true)
        {

                try
                {
                        displacementOperation(redDemo);
                        Thread.sleep(200);
                        repaint();
                }
 

        catch(Exception e){}
        }
        }

        public void displacementOperation(Demo Demo)
        {
                if(Demo.y >= 100 || Demo.y <= 79.5)
        {
                Demo.dy=-Demo.dy;
        }
        Demo.y=Demo.y+Demo.dy;
        }
}