package hr.fer.zemris.optjava.dz5;

public class FullyInformedPSO {

	public static void main(String[] args) {
		// int timesSuccessful = 0;
		// double error;
		// PSO pso = new PSO(100, -10, 10, 1000, 0.5, 0.9, 0.2);
		
		// for(int i = 0; i < 1000; i++) {
		// 	error = pso.solve(0, false);
		// 	if (error < 1E-3) timesSuccessful++;
		// }
		// float percent = (float) timesSuccessful / 1000;
		// System.out.println(percent * 100 + "%");
		
		PSO pso = new PSO(1000, -10, 10, 1000, 0.5, 0.9, 0.2); // najbolji set parametara
		pso.solve(0, true);
		System.out.println("Stvarni minimum je {7, -3, 2, 1, -3, 3} ili {7, -3, 2, 1, 3, 3}");
	}
}
