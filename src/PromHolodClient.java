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
		setSize(1280,530);
		MyPanel panel = new MyPanel();
		Container pane = getContentPane();
		pane.add(panel);
	}
}
class MyPanel extends JPanel{
	public Image im;
	Font font=new Font("Arial", Font.BOLD, 20);
	
	Rectangle server=new Rectangle(500,50,220,35);
	ArrayList<Rectangle> rectangle=new ArrayList<Rectangle>();
	ArrayList<String> String=new ArrayList<String>();
	String mas;	
	Thread thread = new Thread(new Connect());
	
			MyPanel(){				
				addMouseListener( new MyMouse());				
				 mas="m";				
				//try{im = ImageIO.read(new File("Image/tron.jpg"));}catch(IOException exception){}				
				for (int i = 0, t = 0; i < 9; i++,t+=50){
					rectangle.add(new Rectangle(800,10+t,150,50));
				}
			}			
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				setBackground(new Color(150,175,255));
				//g.drawImage(im,0,0,null);
				g.setFont(font);
				g.drawRoundRect(500, 50, 220, 35, 25,25);
				g.drawString("Связатся с Сервером", 505, 75);				
				
				g.setColor(Color.cyan);				
				
				for (int i = 0, t = 0; i < 9; i++,t+=50){					
					g.drawRect(800, 10+t, 150, 50);					
				}
				
				Iterator <String> n = String.iterator();
				int s=0;
				while(n.hasNext()){					
					String  str=n.next();					
					g.drawString(str, 805, 40+s);
					s+=50;
				}	
				
				repaint();
			}	
			
			public class MyMouse extends MouseAdapter  {				
					
				public void mousePressed(MouseEvent event){				
					if(server.contains(event.getPoint())){
						System.out.println("Потытка соеденится");
						thread = new Thread(new Connect());
						thread.start();					
						
					}					    				
				}
			}
}
class Connect implements Runnable{
	Colection colection = new Colection();
	ObjectInputStream oin;		
	ObjectOutputStream oos;	
	private PrintWriter out;
	Socket socket=null;
	
	public void run(){
		try {
			//socket = new Socket("7.102.42.92", 8080);			
			socket = new Socket("127.0.0.1", 8080);
			
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);			
			out.println("+");
			
			oin = new ObjectInputStream(socket.getInputStream());
			colection.mainStr=(ArrayList<String>) oin.readObject();
			System.out.println(colection.mainStr);System.out.println("Данны получены успешно");
			
		} catch (IOException | ClassNotFoundException e1) {e1.printStackTrace();}			         
		finally {							
			try {socket.close();}
			catch (IOException e) {	System.err.println("Socket not closed");}
		}
	}
	
	
}
class Colection implements Serializable{	
	ArrayList<String> mainStr=new ArrayList<String>();
	ArrayList<String> other=new ArrayList<String>();			
}