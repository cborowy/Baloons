
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.util.Random;


import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Klasa reprezentujaca panel na ktorym bedzie odbywala sie gra
 * 
 * @author cezary
 *
 */
public class MyPanel extends JPanel implements ActionListener {
	
	
	/**
	 * utworzenie obiektu ktory odpowiada za pobranie danych z pliku
	 */
	Parser pr = new Parser();
	/**
	 * atrybuty zczytywane z parsera takie jak promien kulki, rozmiar planszy, ilosc mozliwych do losowania kolorow, liczba kulek startowych
	 */
	private int r = pr.getRadius();
	private int x = pr.getXDimension();
	private int y = pr.getYDimension();
	private int ni = 2;
	private int startX = pr.getStartX();
	private int startY = pr.getStartY();
	/**
	 * tablica, ktora przechowuje obiekty typu Baloon reprezentujace kulki
	 */
	Baloon[][] tab = new Baloon[x][y];	
	/**
	 * tablica, ktora informuje w ktorym miejscu jest kulka a w ktorym null
	 */
	boolean[][] baloonExist = new boolean[x][y];
	/**
	 * tablica ktora, sluzy do sprawdzenia czy balon był odwiedzany przez fill flood podczas sprawdzania ktore balony usunac
	 */
	boolean[][] baloonVisited = new boolean[x][y];
	/**
	 * pokazuje ile jest przyleglych do siebie balonow 
	 */
	int d;
	/**
	 * sluzy do sprawdzenia ktore balony beda usuwane
	 */
	boolean[][] baloonDisapear = new boolean[x][y];
	/**
	 * wspolrzedne miejsca, w ktorym bedzie lezal shooterBaloon, miejsca z ktorego bedziemy strzelac
	 */
	private int SHOOTER_X = 240;
	private int SHOOTER_Y = 530;
	/**
	 * obiekt, ktorym mozna strzelac a nastepnie po kolizji jest on kopiowany do tablicy tab[][]
	 */
	private Baloon shooterBaloon;
	/**
	 * Random potrzebny do losowania kolorów
	 */
	Random rand = new Random();
	/**
	 * Wspolrzedne klikniecia
	 */
	private int clickX = 240;
	private int clickY = 530;
	/**
	 * wspolrzedne ograniczajace mape
	 */
	private int left = 0;
	private int right = r*x;
	private int up = 0;
	/**
	 * zmienna mowiaca ile wystrzalow zrobilismy
	 */
	private int shooterControl = 0;
	
	
	/**
	 * znacznik ktory mowi czy gra ma toczyc sie dalej
	 */
	public boolean game = true;
	/**
	 * znacznik ktory mowi czy przegralismy
	 */
	private boolean gameOver = false;
	/**
	 * zmienna, ktora mowi czy shooterBaloon zostal wystrzelony
	 */
	private boolean shoot = false;
	/**
	 * zmienna, ktora mowi czy kulka do strzelania istnieje czy juz jest kulka przyczepiona do reszty
	 */
	public boolean shooterExist = false;
	/**
	 * znacznik ktory mowi czy proces usuwania kulek zostal skonczony czy nie
	 */
	public boolean disapearing = false;
	/**
	 * wskaznik, ktory mowi czy tablica jest pusta, jesli tak to wygralismy gre
	 */
	
	/**
	 * FPS
	 */
	static int fps = 60;
	private int time = 1000000/fps;
	
	Timer timer;
	/**
	 * zmienna mowiaca o tym, ile punktow zdobyl gracz
	 */
	public int scores = 0;
	/**
	 * nick
	 */
	String name = "nick";
	
	/**
	 * konstruktor panelu
	 */
	public MyPanel(){
		super();
		timer = new Timer(time,this);
		timer.start();

		
		/**
		 * Funkcje nasluchujace akcji myszy 
		 */
		addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
			    clickX=e.getX();
			    clickY=e.getY();
			    
			    shooterBaloon.setClickCoord(clickX, clickY);
			    
			    shooterBaloon.setXDir();
			    shooterBaloon.setYDir();
			    shooterBaloon.stop = false;
			    shoot = true;
			    System.out.println(clickX+","+clickY);
			    //addRow();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}	
		});
		game = true;
	}
	
	/**
	 * funkcja generujaca kulki poczatkowe
	 */
	public void generateBaloons(){
		/*
		 * ustawia pustą tablicę balonow
		 */
		for(int j = 0; j < y; j++){
			for(int i = 0; i < x; i++){
				baloonExist [i][j] = false;
			}
		}
		/*
		 * ustawia startowe balony na swoich miejscach
		 */
		
			for(int j = 0; j < startY; j++){
				for(int i = 0; i < startX; i++){
					int n = rand.nextInt(ni);
					tab[i][j] = new Baloon(i*r, j*r, r, n);
					tab[i][j].setPaintCoords(); 
					baloonExist [i][j] = true;
					
				}
			}
			shooter();
	}
	
	/**
	 * funkcja ktora ustawia ilosc kolorow w grze
	 */
	public void setColorRange(int ni){
		this.ni = ni;
	}
	/**
	 * funkcja ktora dopasowuje kulke do istniejacej juz siatki kulek gdy nastepuje kolizja i tworzy nowa kulke do strzelania
	 */
	public void fitToGrid(){
		
		if (shooterBaloon.stop == true && shoot == true){
			
			double dTabX = shooterBaloon.x / r;
			double dTabY = shooterBaloon.y / r;
			
			dTabX = Math.round(dTabX);
			dTabY = Math.round(dTabY);
			
			int iTabX = (int) dTabX;
			int iTabY = (int) dTabY;
			
			Color c = shooterBaloon.c;
			
			tab[iTabX][iTabY] = new Baloon(iTabX*r, iTabY*r, r, c);
			tab[iTabX][iTabY].x = iTabX*r;
			tab[iTabX][iTabY].y = iTabY*r;
			tab[iTabX][iTabY].setPaintCoords();
			tab[iTabX][iTabY].shooter = true;
			
			shoot = false;
			Color currentColor = shooterBaloon.c;
			shooterBaloon = null;
			//baloonsDisapear(iTabX, iTabY);
			
			for(int j = 0; j < y; j++){
				for(int i = 0; i < x; i++){
					baloonVisited[i][j] = false;
				}
			}
			
			for(int j = 0; j < y; j++){
				for(int i = 0; i < x; i++){
					baloonDisapear[i][j] = false;
				}
			}
			
			d = 0;
			floodFill(iTabX,iTabY, currentColor);
			if(d >3){
				for(int j = 0; j < y; j++){
					for(int i = 0; i < x; i++){
						if(baloonDisapear[i][j] == true){
							tab[i][j] = null;
							scores++;
							System.out.println(scores);
						}
					}
				}
			}
		}
	}
	/**
	 * funkcja odpowiadajaca za "zbijanie" kulek oparta na algorytmie Flood-Fill
	 */
	public void floodFill(int disX, int disY, Color currentColor){
		d++;
		baloonDisapear[disX][disY] = true;
		baloonVisited[disX][disY] = true;
		
		if(disX > 0 && disX < this.x - 1 && disY == 0){
			//right
			if(tab[disX + 1][disY] != null && !baloonVisited[disX + 1][disY]) {
				if (tab[disX + 1][disY].c == currentColor){
					floodFill(disX + 1, disY, currentColor);
				}
			}
			
			//left
			if(tab[disX - 1][disY] != null && !baloonVisited[disX - 1][disY]){
				if (tab[disX - 1][disY].c == currentColor ){
				floodFill(disX - 1, disY, currentColor);
					
				}
			}
			
			//down
			if(tab[disX][disY +1] != null && !baloonVisited[disX][disY + 1]){
				if (tab[disX][disY +1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX, disY + 1, currentColor);
					
					
				}
			}
			
			
			//right-down
			if(tab[disX + 1][disY +1] != null && !baloonVisited[disX + 1][disY + 1]){
				if (tab[disX + 1][disY +1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX + 1, disY + 1, currentColor);
					
					
				}
			}
			
			// left-down
			if(tab[disX - 1][disY +1] != null && !baloonVisited[disX - 1][disY + 1]){
				if (tab[disX - 1][disY +1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX - 1, disY + 1, currentColor);
					
					
				}
			}
		}else if(disX == this.x - 1 && disY > 0 && disY < this.x - 1){
			//prawa krawedz
			
			//up
			if(disY >= 0 && tab[disX][disY - 1] != null && !baloonVisited[disX][disY - 1]){
				if (tab[disX][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX, disY - 1, currentColor);
					
				}
			}
			
			//left
			if(tab[disX - 1][disY] != null && !baloonVisited[disX - 1][disY]){
				if (tab[disX - 1][disY].c == currentColor ){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX - 1, disY, currentColor);
					
				}
			}
			
			//down
			if(tab[disX][disY +1] != null && !baloonVisited[disX][disY + 1]){
				if (tab[disX][disY +1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX, disY + 1, currentColor);
					
					
				}
			}
			// left - up
			if(tab[disX - 1][disY - 1] != null && !baloonVisited[disX - 1][disY - 1]){
				if (tab[disX - 1][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX - 1, disY - 1, currentColor);
					
					
				}
			}
			
			// left-down
			if(tab[disX - 1][disY +1] != null && !baloonVisited[disX - 1][disY + 1]){
				if (tab[disX - 1][disY +1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX - 1, disY + 1, currentColor);
					
					
				}
			}
		}else if(disX == 0 && disY > 0 && disY < this.x - 1 ){
			
			//lewa krawedz
			//up
			if(disY >= 0 && tab[disX][disY - 1] != null && !baloonVisited[disX][disY - 1]){
				if (tab[disX][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX, disY - 1, currentColor);
					
				}
			}
			//right
			if(tab[disX + 1][disY] != null && !baloonVisited[disX + 1][disY]) {
				if (tab[disX + 1][disY].c == currentColor){
					
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX + 1, disY, currentColor);
				}
			}
			
			//down
			if(tab[disX][disY +1] != null && !baloonVisited[disX][disY + 1]){
				if (tab[disX][disY +1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX, disY + 1, currentColor);
					
					
				}
			}
			
			// right - up
			if(tab[disX + 1][disY - 1] != null && !baloonVisited[disX + 1][disY - 1]){
				if (tab[disX + 1][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX + 1, disY - 1, currentColor);
					
					
				}
			}
			
			//right-down
			if(tab[disX + 1][disY +1] != null && !baloonVisited[disX + 1][disY + 1]){
				if (tab[disX + 1][disY +1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX + 1, disY + 1, currentColor);
					
					
				}
			}
			
		}else if(disX > 0 && disX < this.x - 1 && disY == this.y - 1){
			//dolna krawedz
			//up
			if(disY >= 0 && tab[disX][disY - 1] != null && !baloonVisited[disX][disY - 1]){
				if (tab[disX][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX, disY - 1, currentColor);
					
				}
			}
			//right
			if(tab[disX + 1][disY] != null && !baloonVisited[disX + 1][disY]) {
				if (tab[disX + 1][disY].c == currentColor){
					
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX + 1, disY, currentColor);
				}
			}
			
			//left
			if(tab[disX - 1][disY] != null && !baloonVisited[disX - 1][disY]){
				if (tab[disX - 1][disY].c == currentColor ){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX - 1, disY, currentColor);
					
				}
			}
			
			// left - up
			if(tab[disX - 1][disY - 1] != null && !baloonVisited[disX - 1][disY - 1]){
				if (tab[disX - 1][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX - 1, disY - 1, currentColor);
					
					
				}
			}


			// right - up
			if(tab[disX + 1][disY - 1] != null && !baloonVisited[disX + 1][disY - 1]){
				if (tab[disX + 1][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX + 1, disY - 1, currentColor);
					
					
				}
			}
			
		}else if(disX == 0 && disY == 0){
			//lewy gorny rog
			//right
			if(tab[disX + 1][disY] != null && !baloonVisited[disX + 1][disY]) {
				if (tab[disX + 1][disY].c == currentColor){
					
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX + 1, disY, currentColor);
				}
			}
			//down
			if(tab[disX][disY +1] != null && !baloonVisited[disX][disY + 1]){
				if (tab[disX][disY +1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX, disY + 1, currentColor);
					
					
				}
			}
			
			//right-down
			if(tab[disX + 1][disY +1] != null && !baloonVisited[disX + 1][disY + 1]){
				if (tab[disX + 1][disY +1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX + 1, disY + 1, currentColor);
					
					
				}
			}

		}else if(disX == this.x - 1 && disY == 0){
			//prawy  gorny rog
			
			//left
			if(tab[disX - 1][disY] != null && !baloonVisited[disX - 1][disY]){
				if (tab[disX - 1][disY].c == currentColor ){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX - 1, disY, currentColor);
					
				}
			}
			
			//down
			if(tab[disX][disY +1] != null && !baloonVisited[disX][disY + 1]){
				if (tab[disX][disY +1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX, disY + 1, currentColor);
					
					
				}
			}
			
			// left-down
			if(tab[disX - 1][disY +1] != null && !baloonVisited[disX - 1][disY + 1]){
				if (tab[disX - 1][disY +1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX - 1, disY + 1, currentColor);
					
					
				}
			}
			
		}else if(disX == this.x -1 && disY == this.y - 1){
			//prawy dolny rog
			//up
			if(disY >= 0 && tab[disX][disY - 1] != null && !baloonVisited[disX][disY - 1]){
				if (tab[disX][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX, disY - 1, currentColor);
					
				}
			}
			
			//left
			if(tab[disX - 1][disY] != null && !baloonVisited[disX - 1][disY]){
				if (tab[disX - 1][disY].c == currentColor ){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX - 1, disY, currentColor);
					
				}
			}
			
			// left - up
			if(tab[disX - 1][disY - 1] != null && !baloonVisited[disX - 1][disY - 1]){
				if (tab[disX - 1][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX - 1, disY - 1, currentColor);
					
					
				}
			}
		}else if(disX == 0 && disY == this.y - 1){
			//up
			if(disY >= 0 && tab[disX][disY - 1] != null && !baloonVisited[disX][disY - 1]){
				if (tab[disX][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX, disY - 1, currentColor);
					
				}
			}
			//right
			if(tab[disX + 1][disY] != null && !baloonVisited[disX + 1][disY]) {
				if (tab[disX + 1][disY].c == currentColor){
					
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX + 1, disY, currentColor);
				}
			}
			// right - up
			if(tab[disX + 1][disY - 1] != null && !baloonVisited[disX + 1][disY - 1]){
				if (tab[disX + 1][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX + 1, disY - 1, currentColor);
					
					
				}
			}
		}else{
			//up
			if(disY >= 0 && tab[disX][disY - 1] != null && !baloonVisited[disX][disY - 1]){
				if (tab[disX][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX, disY - 1, currentColor);
					
				}
			}
			//right
			if(tab[disX + 1][disY] != null && !baloonVisited[disX + 1][disY]) {
				if (tab[disX + 1][disY].c == currentColor){
					
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX + 1, disY, currentColor);
				}
			}
			
			//left
			if(tab[disX - 1][disY] != null && !baloonVisited[disX - 1][disY]){
				if (tab[disX - 1][disY].c == currentColor ){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX - 1, disY, currentColor);
					
				}
			}
			
			//down
			if(tab[disX][disY +1] != null && !baloonVisited[disX][disY + 1]){
				if (tab[disX][disY +1].c == currentColor){
				//System.out.println(disX + " " + disY);
					
					
					floodFill(disX, disY + 1, currentColor);
					
					
				}
			}
			//right-down
			if(tab[disX + 1][disY +1] != null && !baloonVisited[disX + 1][disY + 1]){
				if (tab[disX + 1][disY +1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX + 1, disY + 1, currentColor);
					
					
				}
			}

			// left-down
			if(tab[disX - 1][disY +1] != null && !baloonVisited[disX - 1][disY + 1]){
				if (tab[disX - 1][disY +1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX - 1, disY + 1, currentColor);
					
					
				}
			}

			// left - up
			if(tab[disX - 1][disY - 1] != null && !baloonVisited[disX - 1][disY - 1]){
				if (tab[disX - 1][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX - 1, disY - 1, currentColor);
					
					
				}
			}


			// right - up
			if(tab[disX + 1][disY - 1] != null && !baloonVisited[disX + 1][disY - 1]){
				if (tab[disX + 1][disY - 1].c == currentColor){
					//System.out.println(disX + " " + disY);
					
					
					floodFill(disX + 1, disY - 1, currentColor);
					
					
				}
			}
		}
	}


	/**
	 * funkcja, ktora tworzy nowy balon zeby pozniej mozna bylo go wystrzelic 
	 */
	public void shooter(){
		int n = rand.nextInt(ni);
		shooterBaloon = new Baloon(SHOOTER_X, SHOOTER_Y , r, n);
		//shooterBaloon.setBaloonCoord(tab);
		shooterControl++;
		
	}
	/**
	 * funkcja dodajaca nowy rzad kulek od gory
	 */
	public void addRow(){
		shooterControl = 0;
		
		
		for(int j = y - 2; j > 0; j--){
			for(int i = 0; i < x; i++){
				if(tab[i][j] != null){
					tab[i][j+1] = tab[i][j];
					tab[i][j] = null;
				}
			}
		
		}
		this.repaint();
	}
	/**
	 * funkcja konczaca gre
	 */
	public void stop(){
		this.setVisible(false);
		this.game = false;
		this.gameOver = true;
		System.out.println("STOP");
	}
	
	/**
	 * funkcja spradzajaca czy wszystkie kulki sa zbite i czy wygralismy
	 */
	public void win(){
		int tabNull = 0;
		
		for(int j = 0; j < y; j++){
			for(int i = 0; i < x; i++){
				if(tab[i][j] != null){
				tabNull++;
				}
			}
		}
		if(tabNull == 0) stop();
		tabNull = 0;
	}
	@Override
	public void paint(Graphics g){
		Dimension size = getSize();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, size.width, size.height);
		for(int j = 0; j < y; j++){
			for(int i = 0; i < x; i++){
				if(tab[i][j] != null){
					tab[i][j].paintComponent(g2d);
				}
			}
		}
		if(shooterBaloon != null){
			shooterBaloon.paintComponent(g2d);
		}
		g2d.setColor(Color.BLACK);
		g2d.drawString("Punkty: " + scores, 10, 570);
	}
/**
 * petla gry
 */
	public void game()  {
		
		while(game){
			if (shooterBaloon == null){
				shooter();
				repaint();
			}

			if(shooterBaloon != null){

				shooterBaloon.update();
				shooterBaloon.collisionWall(left, right, up);
				for(int j = y - 1; j > 0; j-- ){
					for(int i = 0; i < startX; i++){
						if(tab[i][j] != null){
							shooterBaloon.collisionBall(tab[i][j]);
						}
					}
				}
				
				for(int i = 0; i < x; i++){
					if(tab[i][y-1] != null){
						stop();
					}
				}
				if(shooterControl > 3){
					//addRow();
				}
				fitToGrid();
				repaint();
			}
			
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(gameOver){
			try{
				HighScore hs = new HighScore();
				hs.changeScores(name, scores);
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
		}
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}	
}

	
	
