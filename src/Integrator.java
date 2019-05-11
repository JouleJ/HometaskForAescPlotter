public class Integrator {
	public static final double EPSILON = 1e-5;
	private static final int MIN_STEP = 256;
	
	private static double intergateStep(IAbstractFunction func, double a, double b, int step) {
		double ret = 0;
		
		for(int i = 0; i <= step; ++i) {
			double x = a + ((double)i * (b - a)) / (double)step;
			double y = func.apply(x);
			
			ret += y;
		}
		
		ret *= (b - a) / (double)step;
		
		return ret;
	}
	
	public static double intergrate(IAbstractFunction func, double a, double b) {
		int step = MIN_STEP;
		double cur = intergateStep(func, a, b, step);
		
		while(true) {
			++step;
			
			double ncur = intergateStep(func, a, b, step);
			
			if(Math.abs(ncur - cur) <= EPSILON)
				break;
			
			cur = ncur;
		}
		
		//System.out.println(step);
		
		return cur;
	}
}
