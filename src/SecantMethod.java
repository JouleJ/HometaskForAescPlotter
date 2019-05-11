import java.util.ArrayList;

public class SecantMethod {
	private static final double EPSILON = 1e-4;
	
	private IAbstractFunction func;
	private ArrayList<Double> ls = new ArrayList<Double>();
	private ArrayList<Double> rs = new ArrayList<Double>();
	
	public SecantMethod(IAbstractFunction _func) {
		func = _func;
	}
	
	public double findZero(double l, double r) {
		
		ls.clear();
		rs.clear();
		
		double oldAbs = 1e18;
		
		ls.add(Double.valueOf(l));
		rs.add(Double.valueOf(r));
		
		while(true) {
			double fL = func.apply(l);
			double fR = func.apply(r);
			
			double m = ((l - r) * fL) / (fR - fL) + l;
			double fM = func.apply(m);
			
			//System.out.println("l,m,r=" + String.valueOf(l) + ";" + String.valueOf(m) + ";" + String.valueOf(r));
			
			if(Math.signum(fM) != Math.signum(fL))
				r = m;
			else
				l = m;
			
			double abs = Math.abs(func.apply(m));
			
			ls.add(Double.valueOf(l));
			rs.add(Double.valueOf(r));
			
			if(Math.abs(abs - oldAbs) <= EPSILON)
				return m;
			
			oldAbs = abs;
		}
		
		//return 0.5 * (l + r);
	}
	
	public ArrayList<Double> getLS() {
		return ls;
	}
	
	public ArrayList<Double> getRS() {
		return rs;
	}
}
