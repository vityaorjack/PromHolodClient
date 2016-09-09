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
	//JTextField textField = new JTextField("",20);
	public MyFrame()  throws ClassNotFoundException, IOException{
		setSize(1280,730);
		MyPanel panel = new MyPanel();
		Container pane = getContentPane();
		pane.add(panel);	
		//panel.add(textField);
		
	}
}
class MyPanel extends JPanel{
	public Image im;
	Font font=new Font("Arial", Font.BOLD, 20);	
	Connect connect=new Connect();
	String console="";	
	ArrayList<Rectangle> rectangles=new ArrayList<Rectangle>();//кнопки
	
	String text="";
	
			MyPanel(){
				addKeyListener(new MyKey());
				setFocusable(true);
				addMouseListener( new MyMouse());								
				//try{im = ImageIO.read(new File("Image/tron.jpg"));}catch(IOException exception){}				
				connect.start(8);				
								
				for (int i = 0, t = 0; i < 7; i++,t+=100){
					rectangles.add(new Rectangle(800,100+t,300,100));
				}
				rectangles.add(new Rectangle(500,50,220,35));//гл.кнопка
				rectangles.add(new Rectangle(500, 150, 220, 35));//добавить
				rectangles.add(new Rectangle(500, 250, 220, 35));//удалить
			}			
			public void paintComponent(Graphics g){
				super.paintComponent(g);  repaint();
				setBackground(new Color(150,175,255));
				//g.drawImage(im,0,0,null);
				//консоль на экране
				g.setColor(Color.red);
				g.drawString(console, 10, 10);						
				//рамка
				g.setColor(Color.cyan);
				for (int i = 0, t = 0; i < 9; i++,t+=100){				
					g.drawRect(800, 100+t, 300, 100);					
				}//поле для ввода
				g.setColor(new Color(255,151,185));
				g.fillRoundRect(800, 40, 300, 50, 30, 30);
				
				//текст в рамке
				g.setFont(font);
				g.setColor(Color.black);
				g.drawString(text, 840, 70);
				g.fillRect(820, 50, 7, 30);
				for(int i = 0, ot = 0; i < connect.main.size(); i++,ot+=100){
					g.drawString(connect.main.get(i), 810, 140+ot);				
				}//меню				
				g.drawRoundRect(500, 50, 220, 35, 25,25);		g.drawString("В главное меню", 510, 75);
				g.drawRoundRect(500, 150, 220, 35, 25,25);		g.drawString("Добавить", 510, 175);
				g.drawRoundRect(500, 250, 220, 35, 25,25);		g.drawString("Удалить", 510, 275);
			}	
			
			public class MyMouse extends MouseAdapter  {				
				
				public void mousePressed(MouseEvent event){				
					for(int i=0;i<rectangles.size();i++){
						console="нажато-"+(i);
						if(rectangles.get(i).contains(event.getPoint())){										
							connect.start((i));break;
						}	
					}					  				
				}
			}
			private class MyKey implements KeyListener{ 
				boolean back;
				
				public void keyPressed(KeyEvent arg0) {
					//System.out.println("P="+arg0.getKeyCode());
					if(arg0.getKeyCode()==8){
						text=text.substring(0, text.length()-1);
						back=true;
					}
				}
				public void keyReleased(KeyEvent arg0) {
					}		
				public void keyTyped(KeyEvent arg0) {
					if(!back)text+=String.valueOf(arg0.getKeyChar());
					back=false;
				}
			}
}
class Connect implements Runnable{
	
	ObjectInputStream oin;		
	ObjectOutputStream oos;
	
	Socket socket=null;
	
	ArrayList<String> main=new ArrayList<String>();
	Aparat aparat=new Aparat();
	
	Thread thread;
	int number;
	boolean whil=true,tip;
	
	Connect(){
		thread = new Thread(this);					
		thread.start();
	}
	public void run(){
		try {
			//socket = new Socket("7.102.42.92", 8080);		
			socket = new Socket("127.0.0.1", 8080);
			
			while(true){				
				oos = new ObjectOutputStream(socket.getOutputStream());	        	 	
				oos.writeObject(aparat);				   
				oos.flush();	
			
				oin = new ObjectInputStream(socket.getInputStream());
				if(!tip)main=(ArrayList<String>) oin.readObject();
				else aparat=(Aparat) oin.readObject();
				whil=false;  sleep();
			}
			
		} catch (IOException | ClassNotFoundException | InterruptedException e1) {e1.printStackTrace();}			         
		finally {							
			try {socket.close();}
			catch (IOException e) {	System.err.println("Socket not closed");}
		}
	}
	void sleep() throws InterruptedException{
		while(!whil)Thread.sleep(500);		
	}
	void start(int number){		
		aparat.integer=number;
		whil=true;
	}	
}
class Aparat implements Serializable{
	int integer;
	String srt;	
}