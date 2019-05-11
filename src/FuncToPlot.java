class G1 implements IAbstractFunction {
	@Override
	public double apply(double x) {
		return Math.exp(0.1 * x) -  x * x - 100;
	}
}

class G2 implements IAbstractFunction {
	private double x;
	
	public G2(double _x) {
		x = _x;
	}
	
	@Override
	public double apply(double y) {
		return Math.sin(x * y);
	}
}

class FuncToPlot implements IAbstractFunction {
	public static double INT_A = 0.0;
	public static double INT_B = Math.PI * 0.5;
	
	@Override
	public double apply(double x) {
		return (new G1()).apply(x) + Integrator.intergrate(new G2(x), INT_A, INT_B);
	}
}