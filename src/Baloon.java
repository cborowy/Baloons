import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
/**
 * 
 * @author cezary
 *Klasa Baloon, obiekt tej klasy reprezentuje jedna kulke na mapie
 */

public class Baloon extends JComponent {
	

	/**
	 * atrybuty r, c, x, y to odpowiednio promien kulki, kolor, wspolrzedne kulki w tablicy
	 */
	private static final long serialVersionUID = 1L;
	private int r;
	public Color c;
	public double x;
	public double y;
	/**
	 * wspolrzedne x i y, za pomoca ktorych bedziemy rysowac nasza kulke
	 * potrzebne ze wzgledu na to, ze reszta zmiennych jest typu double i trzeba przeprowadzic konwersje
	 */
	public int paintX;
	public int paintY;
	/**
	 * wspolrzedne punktu, w ktory kliknelismy mysza
	 */
	private int clickX = 0;
	private int clickY = 0;
	/**
	 * kwanty przesuniecia kulki
	 */
	public double xDir = 0;
	public double yDir = 0;
	/**
	 * zmienne statyczne, ktore okreslaja polozenie kulki, ktora bedzie wystrzeliwana
	 */
	private static int SHOOTER_X = 240;
	private static int SHOOTER_Y = 530;
	/**
	 * zmienne potrzebne do obliczenia kwantu przesuniecia xDir i yDir
	 */
	private double dl = 0;
	private double dx = 0;
	private double dy = 0;
	/**
	 * mowi czy kulka jest w ruchu czy nie
	 */
	public boolean stop = true;
	
	/**
	 * znacznik ktory bedzie mowil czy kulka ma zniknac
	 */
	public boolean disapear;
	/**
	 * znacznik, ktory mowi o tym czy kulka shooter istnieje
	 */
	public boolean shooter = false;
	
	/**
	 * tablica dostepnych kolorow
	 */
	
	private Color[] color = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.ORANGE, Color.MAGENTA};
	/**
	 * konstruktor, przyjmuje podane nizej parametry aby utworzyc kulke o odpowiedniej wielkosci w odpowiednim miejscu
	 * @param x
	 * @param y
	 * @param r
	 * @param n
	 */
	public Baloon(int x, int y, int r, int n){
		super();
		this.x = x;
		this.y = y;
		this.c = color[n];
		this.r = r;	
	}
	/**
	 * konstruktor, przyjmuje podane nizej parametry aby utworzyc kulke o odpowiedniej wielkosci w odpowiednim miejscu
	 * @param x
	 * @param y
	 * @param r
	 * @param c
	 */
	public Baloon(int x, int y, int r, Color c){
		super();
		this.x = x;
		this.y = y;
		this.c = c;
		this.r = r;	
	}
/**
 * funkcja odpowiadajaca za kolizje ze scianami odpowiednio: lewa prawa i gorna
 * @param left
 * @param right
 * @param up
 */
	public void collisionWall(int left, int right, int up){
		
		
		if(this.x <= left){
			if(xDir < 0){
				xDir = -xDir;
			}
		}else if(this.x >= right - this.r){
			if(xDir > 0){
				xDir = -xDir;
			}
		}
		
		if(this.y <= up){
			this.stop();
		}
	}
	/**
	 * funkcja ktora jest odpowiedzialna za kolizje z balonami
	 * jako parametr przyjmuje inny Baloon zeby sprawdzic jego polozenie
	 * @param b
	 */
	public void collisionBall(Baloon b){
		if ((Math.abs(this.x + this.r - b.x) <= 2*r) && (Math.abs(this.y + this.r - b.y) <= 2*r)){
			this.stop();
			//System.out.println(this.x+" " + this.y + " " +b.x + " " + b.y+" " + Math.abs(this.x - b.x));
		}
	}
	/**
	 * funkcja aktualizujaca obecne polozenie kulki, ktora jest w ruchu
	 */
	public void update(){
		this.move();
		
		this.paintX = (int)x;
		this.paintY = (int)y;
	}
	
	/**
	 * funkcja ktora przesuwa kulke
	 */
	public void move(){
				this.x += xDir;
				this.y += yDir;	
	}
	/**
	 * funkcja zatrzymujaca kulke
	 */
	public void stop(){
		xDir = 0;
		yDir = 0;
		stop = true;
		
	}
	/**
	 * funkcje ustawiajace kwant xDir  ktory pozniej jest dodawane do kulki aby mogla sie poruszac
	 */
	public void setXDir(){
		dx = clickX - SHOOTER_X;
		dy = clickY - SHOOTER_Y;
		dl = Math.sqrt(dx*dx + dy*dy);
		
		xDir = dx/dl;
		//System.out.println("xdir" + xDir);
	}
	
	/**
	 * funkcje ustawiajace kwant yDir ktory pozniej jest dodawany do kulki aby mogla sie poruszac
	 */
	
	public void setYDir(){
		dx = clickX - SHOOTER_X;
		dy = clickY - SHOOTER_Y;
		dl = Math.sqrt(dx*dx + dy*dy);
	
		yDir = dy/dl;
	}
	/**
	 * funkcje zwracajace kwant ruchu
	 * @return
	 */
	public double getXDir(){
		return xDir;
	}
	/**
	 * funkcje zwracajace kwant ruchu
	 * @return
	 */
	public double getYDir(){
		return yDir;
	}
	
	
	/**
	 * funkcja, ktora informuje kulke w ktore miejsce ma sie przemiescic
	 * @param clickX
	 * @param clickY
	 */
	public void setClickCoord(int clickX, int clickY){
		this.clickX = clickX;
		this.clickY = clickY;
	}
	/**
	 * metoda ustawia paintX i paintY na x i y
	 */
	public void setPaintCoords(){
		this.paintX = (int)x;
		this.paintY = (int)y;
	}
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		//ładne kształty kół
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);
		//ustawia kolor danej kulki
		g2d.setColor(c);
		//rysuje kulke
		g2d.fillOval(this.paintX,this.paintY,this.r, this.r);	
	}
}




