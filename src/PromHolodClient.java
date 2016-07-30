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
	
	Rectangle server=new Rectangle(500,50,220,35);
	ArrayList<Rectangle> rectangle=new ArrayList<Rectangle>();	
	
	Thread thread = new Thread(new Connect());	
	
			MyPanel(){				
				addMouseListener( new MyMouse());								
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
				//рамка
				for (int i = 0, t = 0; i < 9; i++,t+=100){					
					g.drawRect(800, 100+t, 300, 100);					
				}//текст в рамке
				g.setColor(Color.black);
				for(int i = 0, ot = 0; i < Colection.mainStr.size(); i++,ot+=100){
					g.drawString(Colection.mainStr.get(i), 810, 140+ot);
					
					
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
	
	ObjectInputStream oin;		
	ObjectOutputStream oos;	
	private PrintWriter out;
	Socket socket=null;
	
	public void run(){
		try {
			//socket = new Socket("7.102.42.92", 8080);			
			socket = new Socket("127.0.0.1", 8080);
			
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);			
			out.println("1");
			
			oin = new ObjectInputStream(socket.getInputStream());
			Colection.mainStr=(ArrayList<String>) oin.readObject();
			System.out.println(Colection.mainStr);System.out.println("Данны получены успешно");
			
		} catch (IOException | ClassNotFoundException e1) {e1.printStackTrace();}			         
		finally {							
			try {socket.close();}
			catch (IOException e) {	System.err.println("Socket not closed");}
		}
	}
	
	
}
class Colection{	
	static ArrayList<String> mainStr=new ArrayList<String>();
	static ArrayList<String> other=new ArrayList<String>();			
}