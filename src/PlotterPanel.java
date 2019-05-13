import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class PlotterPanel extends Canvas {

	public static final int PANEL_SIZE = 512;
	public static final int CIRCLE_SIZE = 8;
	
	private IAbstractFunction f = null;
	
	private int x1 = 0;
	private int y1 = 0;
	
	private int x2 = PANEL_SIZE - 1;
	private int y2 = PANEL_SIZE - 1;
	
	private double a = 0;
	private double b = 1;
	
	private double c = 0;
	private double d = 1;
	
	private boolean runSecantMethod = false;
	
	public PlotterPanel(IAbstractFunction new_f) {
		setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
		f = new_f;
		
		findCAndD();
	}
	
	public void setRectangle(int new_x1, int new_y1, int new_x2, int new_y2) {
		x1 = new_x1;
		y1 = new_y1;
		
		x2 = new_x2;
		y2 = new_y2;
		
		findCAndD();
	}
	
	private void findCAndD() {
		c = 1e18;
		d = -1e18;
		
		for(int x = x1; x <= x2; ++x)
		{
			double mathx = pixelXToMathematical(x);
			double mathy = f.apply(mathx);
						
			c = Math.min(c, mathy);
			d = Math.max(d, mathy);
		}
		
		double minlen = 1e-7;
		
		if(d < c + minlen)
			d = c + minlen;
	}
	
	public void setInterval(double new_a, double new_b) {
		a = new_a;
		b = new_b;
		
		findCAndD();
	}
	
	private double pixelXToMathematical(int x) {
		return ((double)(x - x1) / (double)(x2 - x1)) * (b - a) + a;
	}
	
	/*private double pixelYToMathematical(int y) {
		return ((double)(y - y1) /(double)(y2 - y1)) * (d - c) + c;
	}*/

	private int MathematicalXtoPixel(double mathx) {
		return (int)Math.floor((x2 - x1) * (mathx - a) / (b - a) + x1);
	}
	
	private int MathematicalYtoPixel(double mathy) {
		return y1 + y2 - (int)Math.floor((y2 - y1) * ((mathy - c) / (d - c)) + y1);
	}
	
	public void enableSecantMethod() {
		runSecantMethod = true;
	}
	
	@Override
	public void paint(Graphics g) {		
		super.paint(g);
		setBackground(Color.WHITE);
		
		ArrayList<Integer> xs = new ArrayList<Integer>();
		ArrayList<Integer> ys = new ArrayList<Integer>();
		int pts = 0;
		
		for(int x = x1; x <= x2; ++x)
		{
			double mathx = pixelXToMathematical(x);
			double mathy = f.apply(mathx);
			
			int y = MathematicalYtoPixel(mathy);
			
			xs.add(x);
			ys.add(y);
			++pts;
		}
		
		Graphics2D g2d = (Graphics2D)g;
		
		GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, pts);
		
		polyline.moveTo(xs.get(0), ys.get(0));
		
		for(int i = 1; i < pts; ++i)
			polyline.lineTo(xs.get(i), ys.get(i));;
		
		g2d.setColor(Color.BLUE);
		g2d.setStroke(new BasicStroke(2));
		g2d.draw(polyline);
		
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(1));
		
		if(a <= 0 && 0 <= b) {
			int zero = MathematicalXtoPixel(0);
			
			Line2D vert = new Line2D.Double(zero, y1, zero, y2);
			g2d.draw(vert);
		}
		
		if(c <= 0 && 0 <= d) {
			int zero = MathematicalYtoPixel(0);
			
			Line2D hor = new Line2D.Double(x1, zero, x2, zero);
			g2d.draw(hor);
		}
		
		if(runSecantMethod) {
			
			if(Math.signum(f.apply(a)) != Math.signum(f.apply(b))) {
				SecantMethod secantMethod = new SecantMethod(f);
			
				double mathx = secantMethod.findZero(a, b);
				double mathy = f.apply(mathx);
			
				System.out.println(mathx);
				System.out.println(mathy);
			
				int x = MathematicalXtoPixel(mathx);
				int y = MathematicalYtoPixel(mathy);
			
				g2d.drawOval(x - CIRCLE_SIZE, y- CIRCLE_SIZE, 2 * CIRCLE_SIZE, 2 * CIRCLE_SIZE);
				
				ArrayList<Double> ls = secantMethod.getLS();
				ArrayList<Double> rs = secantMethod.getRS();
				
				int sz = ls.size();
				
				g2d.setColor(Color.RED);
				
				for(int i = 0; i < sz; ++i) {
					double x1math = ls.get(i).doubleValue();
					double y1math = f.apply(x1math);
					
					double x2math = rs.get(i).doubleValue();
					double y2math = f.apply(x2math);
					
					int x1 = MathematicalXtoPixel(x1math);
					int y1 = MathematicalYtoPixel(y1math);
					
					int x2 = MathematicalXtoPixel(x2math);
					int y2 = MathematicalYtoPixel(y2math);
					
					//System.out.println(x1math);
					//System.out.println(y1math);
					
					//System.out.println(x2math);
					//System.out.println(y2math);
					
					Line2D secant = new Line2D.Double(x1, y1, x2, y2);
					g2d.draw(secant);
				}
				
			} else {
				System.out.println("Error: cannot run secant method!");
			}
			
			runSecantMethod = false;
		}
	}
}
