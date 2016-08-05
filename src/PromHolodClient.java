import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.*;


public class PromHolodClient {
		
	public static void main(String[] args) throws ClassNotFoundException, IOException{		
		MyFrame frame = new MyFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ПромХолод");			
		frame.show();		
	}
}
class MyFrame extends JFrame{
		
	public MyFrame()  throws ClassNotFoundException, IOException{
		setSize(1280,730);
		MyPanel panel = new MyPanel();
		Container pane = getContentPane();
		pane.add(panel);
	}
}
class MyPanel extends JPanel{
	public Image im;
	Font font=new Font("Arial", Font.BOLD, 20);	
	Connect connect=new Connect();
	String console="";
	Rectangle rectangle=new Rectangle(500,50,220,35);//гл.кнопка
	ArrayList<Rectangle> rectangles=new ArrayList<Rectangle>();//кнопки
	
			MyPanel(){				
				addMouseListener( new MyMouse());								
				//try{im = ImageIO.read(new File("Image/tron.jpg"));}catch(IOException exception){}				
				connect.start(0);				
								
				for (int i = 0, t = 0; i < 7; i++,t+=100){
					rectangles.add(new Rectangle(800,100+t,300,100));
				}
			}			
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				setBackground(new Color(150,175,255));
				//g.drawImage(im,0,0,null);
				//консоль на экране
				g.setColor(Color.red);
				g.drawString(console, 10, 10);								
				//рамка
				g.setColor(Color.cyan);
				for (int i = 0, t = 0; i < 9; i++,t+=100){				
					g.drawRect(800, 100+t, 300, 100);					
				}
				
				//текст в рамке
				g.setFont(font);
				g.setColor(Color.black);
				for(int i = 0, ot = 0; i < Colection.mainStr.size(); i++,ot+=100){
					g.drawString(Colection.mainStr.get(i), 810, 140+ot);					
				}//меню				
				g.drawRoundRect(500, 50, 220, 35, 25,25);
				g.drawString("В главное меню", 510, 75);
				//обновление экрана
				repaint();
			}	
			
			public class MyMouse extends MouseAdapter  {				
				
				public void mousePressed(MouseEvent event){				
					for(int i=0;i<rectangles.size();i++){
						console="нажато-"+(i+1);
						if(rectangles.get(i).contains(event.getPoint())){										
							connect.start((i+1));break;
						}	
					}
					if(rectangle.contains(event.getPoint())){connect.start(0);}    				
				}
			}
}
class Connect implements Runnable{
	
	ObjectInputStream oin;		
	ObjectOutputStream oos;	
	private PrintWriter out;
	Socket socket=null;
	
	Thread thread;
	int number;
	boolean whil=true;
	
	Connect(){
		thread = new Thread(this);					
		thread.start();
	}
	public void run(){
		try {
			//socket = new Socket("7.102.42.92", 8080);		
			socket = new Socket("127.0.0.1", 8080);
			
			while(true){			
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);			
				out.println(number);
			
				oin = new ObjectInputStream(socket.getInputStream());
				Colection.mainStr=(ArrayList<String>) oin.readObject();
				whil=false;  sleep(); 
			}
			
		} catch (IOException | ClassNotFoundException | InterruptedException e1) {e1.printStackTrace();}			         
		finally {							
			try {socket.close();}
			catch (IOException e) {	System.err.println("Socket not closed");}
		}
	}
	void sleep() throws InterruptedException{
		Thread.sleep(500);
		if(!whil)sleep();
	}
	void start(int number){		
		this.number=number;
		whil=true;
	}	
}
class Colection{	
	static ArrayList<String> mainStr=new ArrayList<String>();
	static ArrayList<String> other=new ArrayList<String>();	
}