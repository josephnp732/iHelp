package iHelp;

//import com.intellij.ui.JBColor;

//import javafx.embed.swing.JFXPanel;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

@SuppressWarnings("ALL")
public class Bounce extends Applet implements Runnable, ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Demo redDemo, Back;
	public Demo lh_pin, ec_pin, cs_pin, hostel_pin, bs_pin, mech_pin, canteen_pin;
	public Demo principal_info;
    public Image img, I, dest=null;
    public Button button1;
    public int flag=0;
    public URL base1, base2, base3;
    
    public URL iH1, iH2, iH3, iH4, iH5, iH6, iH7, iH8, iH9;
    public Demo CSE, ISE, TCE, EEE, ECE, Mech, CIV, Princi, VP;
    public Image cse, ise, tce, eee, ece, mech, civ, princi, vp;
    
    Font f1;
    Color clr1;
	
        public void init() {
        	
            f1 = new Font("SansSerif", Font.BOLD, 11);
            {
                try {
                    base1 = Bounce.class.getResource("img/Campus.png");
                    base2 = Bounce.class.getResource("img/pin.png");
                    base3 = Bounce.class.getResource("img/dest.gif");
                    
                    iH1 = Bounce.class.getResource("img/CSE.PNG");
                    iH2 = Bounce.class.getResource("img/ISE.PNG");
                    iH3 = Bounce.class.getResource("img/TCE.PNG");
                    iH4 = Bounce.class.getResource("img/ECE.PNG");
                    iH5 = Bounce.class.getResource("img/EEE.PNG");
                    iH6 = Bounce.class.getResource("img/Mech.PNG");
                    iH7 = Bounce.class.getResource("img/CIV.PNG");
                    iH8 = Bounce.class.getResource("img/Principal.PNG");
                    iH9 = Bounce.class.getResource("img/VP.PNG");
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }

                
                button1 = new Button("Click & Ask");
                redDemo = new Demo(300, 150, 30, 45, 2, 4);
                Back = new Demo(1, 1, 1000, 650, 0, 0);

                mech_pin = new Demo(825, 405, 60, 65, 0, 0);
                lh_pin = new Demo(825, 295, 60, 65, 0, 0);
                ec_pin = new Demo(825, 185, 60, 65, 0, 0);
                cs_pin = new Demo(685, 70, 60, 65, 0, 0);
                hostel_pin = new Demo(825, 520, 60, 65, 0, 0);
                bs_pin = new Demo(380, 30, 60, 65, 0, 0);
                canteen_pin = new Demo(650, 405, 60, 65, 0, 0);
                
                CSE = new Demo(1020,20,325,610,0,0);
                ISE = new Demo(1020,20,325,610,0,0);
                TCE = new Demo(1020,20,325,610,0,0);
                EEE = new Demo(1020,20,325,610,0,0);
                ECE = new Demo(1020,20,325,610,0,0);
                Princi = new Demo(1020,20,325,610,0,0);
                Mech = new Demo(1020,20,325,610,0,0);
                CIV = new Demo(1020,20,325,610,0,0);
                VP = new Demo(1020,20,325,610,0,0);

                dest = getImage(base3, "dest.gif");
                I = getImage(base1, "Campus.png");
                img = getImage(base2, "pin.png");
                
                cse = getImage(iH1, "CSE.PNG" );
                ise = getImage(iH2,"ISE.PNG" );
                tce = getImage(iH3,"TCE.PNG" );
                ece = getImage(iH4,"ECE.PNG" );
                eee = getImage(iH5, "EEE.PNG");
                mech = getImage(iH6,"Mech.PNG" );
                civ = getImage(iH7,"CIV.PNG" );
                princi = getImage(iH8,"Principal.PNG" );
                vp = getImage(iH9,"VP.PNG" );
                
                Thread t = new Thread(this);
                add(button1);
                button1.setBackground(Color.decode("#5482e5"));
                button1.addActionListener(this);
                //button1.setBounds(0,40,40,60);
                
                t.start();
            }
        }
        
        	
        	

            @Override
            public void actionPerformed (ActionEvent e){
            	Backend bck = new Backend();
            	flag = bck.Dest();
                if (e.getSource() == button1) {
                    //flag = 1;
                }
                


            }

        public void paint(Graphics g)
        {
        	//Opens in full screen mode
        	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        	Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
            int taskBarSize = scnMax.bottom;
            setSize(screenSize.width, screenSize.height - taskBarSize);
            setLocation(screenSize.width - getWidth(), screenSize.height - taskBarSize - getHeight());
            
            
                //setBackground(JBColor.white);
                setForeground(Color.decode("#5482e5"));
                g.drawImage(I,Back.x,Back.y,Back.p,Back.q,null);
                g.drawImage(img,redDemo.x,redDemo.y,redDemo.p,redDemo.q ,null);

                g.setFont(f1);
            	g.drawString("You are here", 280,210);

            	//process voice data and return coordinates
               //locate(coordinate1, coordinate2)
            	
            	if(flag>=1 && flag<=100)  //Basic Science Block
                {
                 g.drawImage(dest, bs_pin.x, bs_pin.y, bs_pin.p, bs_pin.q, null);
                 g.fillRoundRect(340,180,60,12,10,10);
                 g.fillRoundRect(390,140,12,50,10,10);
                 
                 if(flag>=1 && flag<=2)
	                {
	                	iHelp_INFO("princi", g);
		                
	                }
                 if(flag==3)
	                {
	                	iHelp_INFO("vp", g);
		                
	                }
                 
                }
            	
            	 if(flag>=101 && flag<=200)  //LH Block
                 {
	                 g.drawImage(dest, cs_pin.x, cs_pin.y, cs_pin.p, cs_pin.q, null);
	                 g.fillRoundRect(340,180,380,12,10,10);
	                 g.fillRoundRect(710,140,12,50,10,10);
	                 if(flag>=101 && flag<=110)
	                {
	                	iHelp_INFO("eee", g);
		                
	                }
	                if(flag>=111 && flag<=120)
	                {
		                	iHelp_INFO("ise", g);
			                
		            }
	                if(flag>=121 && flag<=130)
	                {
		                	iHelp_INFO("ece", g);
			                
		            }
	                if(flag>=131 && flag<=140)
	                {
		                	iHelp_INFO("tce", g);
			                
		            }
	                if(flag>=141 && flag<=150)
	                {
		                	iHelp_INFO("cse", g);
			                
		            }
                 }
            	 
            	if(flag>=201 && flag<=300) //Mechanical Block
	            {
	                g.drawImage(dest, mech_pin.x, mech_pin.y, mech_pin.p, mech_pin.q, null);
	                g.fillRoundRect(340,180,449,12,10,10);
	                g.fillRoundRect(780,180,12,290,10,10);
	                g.fillRoundRect(780,460,40,12,10,10);
	                if(flag>=201 && flag<=210)
	                {
	                	iHelp_INFO("mechanical", g);
		                
	                }
	                if(flag>=211 && flag<=220)
	                {
	                	iHelp_INFO("civil", g);
		                
	                }
	                
	            }
                
                /*
                if(flag==111) //EC Block
                {
                    g.drawImage(dest, ec_pin.x, ec_pin.y, ec_pin.p, ec_pin.q, null);
                    g.fillRoundRect(340,180,449,12,10,10);
                    g.fillRoundRect(780,180,12,65,10,10);
                    g.fillRoundRect(780,235,40,12,10,10);
                }
                */

                if(flag>=301 && flag<=400) //Hostel
                {
                    g.drawImage(dest, hostel_pin.x, hostel_pin.y, hostel_pin.p, hostel_pin.q, null);
                    g.fillRoundRect(340,180,449,12,10,10);
                    g.fillRoundRect(780,180,12,405,10,10);
                    g.fillRoundRect(780,575,40,12,10,10);
                }

                if(flag>=401 && flag<=500) //Canteen
                {
                    g.drawImage(dest, canteen_pin.x, canteen_pin.y, canteen_pin.p, canteen_pin.q, null);
                    g.fillRoundRect(340,180,449,12,10,10);
                    g.fillRoundRect(780,180,12,290,10,10);
                    g.fillRoundRect(720,460,70,12,10,10);
                }
                
                
                button1.setLocation(450,605);
        }
 
        public void run()
        {
        	//Backend bck = new Backend();
        	//flag = bck.Dest();

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

        public void iHelp_INFO(String stuff, Graphics z) {
            //System.out.println(stuff);
            if (stuff == "mechanical") {
                z.drawImage(mech, Mech.x, Mech.y, Mech.p, Mech.q, null);

            } else if (stuff == "ise") {
                z.drawImage(ise, ISE.x, ISE.y, ISE.p, ISE.q, null);

            } else if (stuff == "cse") {
                z.drawImage(cse, CSE.x, CSE.y, CSE.p, CSE.q, null);

            } else if (stuff == "civ") {
                z.drawImage(civ, CIV.x, CIV.y, CIV.p, CIV.q, null);

            } else if (stuff == "tce") {
                z.drawImage(tce, TCE.x, TCE.y, TCE.p, TCE.q, null);

            } else if (stuff == "ece") {
                z.drawImage(ece, ECE.x, ECE.y, ECE.p, ECE.q, null);

            } else if (stuff == "eee") {
                z.drawImage(eee, EEE.x, EEE.y, EEE.p, EEE.q, null);

            } else if (stuff == "vp") {
                z.drawImage(vp, VP.x, VP.y, VP.p, VP.q, null);

            } else if (stuff == "princi") {
                z.drawImage(princi, Princi.x, Princi.y, Princi.p, Princi.q, null);

            }
        }	
}