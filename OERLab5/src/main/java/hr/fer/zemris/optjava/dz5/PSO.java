package hr.fer.zemris.optjava.dz5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class PSO {
	
	double[][] data; 
    double[] y_data;
    IFunction system;
    int popSize;
    double c1 = 2.05;
    double c2 = 2.05;
    double k = 0.729;
    double xmin, xmax;
    double vmin, vmax;
    double wmin, wmax;
    int maxIter;    
	
    public PSO(int popSize, double xmin, double xmax, int maxIter, double wmin, double wmax, double vfactor) {
		this.system = loadSystem();
		this.popSize = popSize;
		this.xmin = xmin;
		this.xmax = xmax;
		this.maxIter = maxIter;
		this.wmin = wmin;
		this.wmax = wmax;
		
		vmin = xmin * vfactor;
		vmax = xmax * vfactor;		
	}

    public double solve(int algoType, boolean verbose) {
    	RealVector solution = null;
    	switch(algoType) {
    		case 0:
    			solution = globalPSO(verbose);
    			break;
    		case 1:
    			solution = neighborPSO(verbose);
    			break;
    	}
    	if (verbose) System.out.println("Dobiven minimum: " + solution.toString());
    	if (verbose) System.out.println("Pogreska u pronadenom minimumu: " + system.getValue(solution));
    	return system.getValue(solution);
    }
    
    public RealVector globalPSO(boolean verbose) {
    	// inicijalizacija populacije
    	int dim = system.getNumberOfVariables();
    	double[][] x = new double[popSize][dim];
    	double[][] v = new double[popSize][dim];    	
    	
    	double[] f = new double[popSize];
    	
    	double[] pbest_f = new double[popSize];
    	double[][] pbest = new double[popSize][dim];
    	
    	double gbest_f = 0.0;
    	double[] gbest = new double[dim];
    	
    	double w = wmax;
    	
    	for(int i = 0; i < popSize; i++) {
    		for(int d = 0; d < dim; d++) {
    			x[i][d] = xmin + Math.random() * (xmax - xmin);
    			v[i][d] = vmin + Math.random() * (vmax - vmin);
    		}
    	}
    	
    	for(int iter = 1; iter <= maxIter; iter++) {
    		
    		// evaluiraj populaciju
    		for(int i = 0; i < popSize; i++) {
    			f[i] = system.getValue(MatrixUtils.createRealVector(x[i]));
    		}
    		
    		// provjeri je li dobiveno bolje individualno rjesenje
    		for(int i = 0; i < popSize; i++) {
    			if (f[i] < pbest_f[i] || pbest_f[i] == 0.0) {
    				pbest_f[i] = f[i];
    				pbest[i] = x[i];
    			} 
    		}
    		// provjeri je li nadeno bolje globalno rjesenje
    		for(int i = 0; i < popSize; i++) {
    			if(f[i] < gbest_f || gbest_f == 0.0) {
    				gbest_f = f[i];
    				gbest = x[i].clone();
    			}
    		}    		

    		if(verbose) {
    			System.out.println(iter + ". iteracija. Najbolje pronadeno rjesenje je " + MatrixUtils.createRealVector(gbest).toString() + " s pogreskom " + gbest_f);
    		}
    		
    		// azuriraj brzinu i poziciju cestica
    		for(int i = 0; i < popSize; i++) {
    			for(int d = 0; d < dim; d++) {
    				double rand = Math.random();
    				v[i][d] = v[i][d] * w + k * (c1 * rand * (pbest[i][d] - x[i][d]) + c2 * rand * (gbest[d] - x[i][d]));
    				if (v[i][d] < vmin) v[i][d] = vmin;
    				if (v[i][d] > vmax) v[i][d] = vmax;
    				x[i][d] += v[i][d];
    			}
    		}    
    		
    		w = (iter / maxIter) * (wmin - wmax) + wmax;
    	}
    	
    	return MatrixUtils.createRealVector(gbest);
    }
    
    public RealVector neighborPSO(boolean verbose) {
    	// inicijalizacija populacije
    	int dim = system.getNumberOfVariables();
    	double[][] x = new double[popSize][dim];
    	double[][] v = new double[popSize][dim];    	
    	
    	double[] f = new double[popSize];
    	
    	double[] pbest_f = new double[popSize];
    	double[][] pbest = new double[popSize][dim];
    	
    	double gbest_f = 0.0;
    	double[] gbest = new double[dim];
    	
    	int neighborhoodSize = (int) ((float) 0.15 * popSize);
    	int ns = (neighborhoodSize - 1) / 2;
    	
    	double[] lbest_f = new double[popSize];
    	double[][] lbest = new double[popSize][dim];
    	
    	double w = wmax;
    	
    	for(int i = 0; i < popSize; i++) {
    		for(int d = 0; d < dim; d++) {
    			x[i][d] = xmin + Math.random() * (xmax - xmin);
    			v[i][d] = vmin + Math.random() * (vmax - vmin);
    		}
    	}
    	
    	for(int iter = 1; iter <= maxIter; iter++) {
    		
    		// evaluiraj populaciju
    		for(int i = 0; i < popSize; i++) {
    			f[i] = system.getValue(MatrixUtils.createRealVector(x[i]));
    		}
    		
    		// provjeri je li dobiveno bolje individualno rjesenje
    		for(int i = 0; i < popSize; i++) {
    			if (f[i] < pbest_f[i] || pbest_f[i] == 0.0) {
    				pbest_f[i] = f[i];
    				pbest[i] = x[i];
    			} 
    		}
    		
    		// provjeri je li nadeno bolje globalno rjesenje - ovdje sluzi samo za konacno rjesenje
    		for(int i = 0; i < popSize; i++) {
    			if(f[i] < gbest_f || gbest_f == 0.0) {
    				gbest_f = f[i];
    				gbest = x[i].clone();
    			}
    		}
    		
    		// provjeri je li nadeno bolje lokalno rjesenje
    		for(int i = 0; i < popSize; i++) {
    			if (lbest_f[i] == 0.0) lbest_f[i] = f[i];
    			// susjedstvo ide od i - ns (ili 0) do i + ns (ili popSize - 1) 
    			int j = i - ns;
    			
    			if (j < 0) {
    				j = 0;
    			}
    			
    			while(j <= i + ns && j < popSize) {
    				if(f[j] < lbest_f[i]) {
    					lbest_f[i] = f[j];
    					lbest[i] = x[j].clone();
    				}
    				j++;
    			};
    		}

    		if(verbose) {
    			System.out.println(iter + ". iteracija. Najbolje pronadeno rjesenje je " + MatrixUtils.createRealVector(gbest).toString() + " s pogreskom " + gbest_f);
    		}
    		
    		// azuriraj brzinu i poziciju cestica
    		for(int i = 0; i < popSize; i++) {
    			for(int d = 0; d < dim; d++) {
    				double rand = Math.random();
    				v[i][d] = v[i][d] * w + k * (c1 * rand * (pbest[i][d] - x[i][d]) + c2 * rand * (lbest[i][d] - x[i][d]));
    				if (v[i][d] < vmin) v[i][d] = vmin;
    				if (v[i][d] > vmax) v[i][d] = vmax;
    				x[i][d] += v[i][d];
    			}
    		}    
    		
    		w = (iter / maxIter) * (wmin - wmax) + wmax;
    	}
    	
    	return MatrixUtils.createRealVector(gbest);
    }


	private IFunction loadSystem() {
    	data = new double[20][5];
    	y_data = new double[20];
    	String filename = "02-zad-prijenosna.txt";
	    BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filename));
			String line;
			line = br.readLine();
			int i = 0;
		    while (line != null) {
		    	double[] row = new double[10];
		    	if (line.startsWith("#")) {
		    	}
		    	else if (line.startsWith("[")) {
		    		line = line.replace("[", "");
		    		line = line.replace("]", "");
		    		String[] parts = line.split(",");
		    		int j = 0;
		    		for (String part : parts) {
		    			if (j == 5) {
		    				y_data[i] = Double.parseDouble(part);
		    			}
		    			else {
		    				row[j] = Double.parseDouble(part);
		    			}
		    			j++;
		    		}
		    		data[i] = row;	
		    		i++;
		    	}
		    	line = br.readLine();
		    }
		    br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    final RealMatrix x = MatrixUtils.createRealMatrix(data);
	    final RealVector y = MatrixUtils.createRealVector(y_data);
	    
	    IFunction func = new IFunction() {
			public int getNumberOfVariables() {
				return 6;
			}
	    	
			public double getValue(RealVector coefs) {
				// F(x) = 1/2 * G'(x) * G(x)				
				RealVector G = getG(coefs);
				return 0.5 * G.dotProduct(G) / 20;
			}   	
			
			public RealVector getG(RealVector coefs) {				
				double[] data_G = new double[20];				
				double a = coefs.getEntry(0);
				double b = coefs.getEntry(1);
				double c = coefs.getEntry(2);
				double d = coefs.getEntry(3);
				double e = coefs.getEntry(4);
				double f = coefs.getEntry(5);				
				for (int i = 0; i < 20; i++) {
					double x1 = x.getEntry(i, 0);					
					double x2 = x.getEntry(i, 1);
					double x3 = x.getEntry(i, 2);
					double x4 = x.getEntry(i, 3);
					double x5 = x.getEntry(i, 4);
					
					data_G[i] = a * x1 + b * Math.pow(x1, 3) * x2 + c * Math.exp(d * x3) * (1 + Math.cos(e * x4)) + f * x4 * Math.pow(x5, 2) - y.getEntry(i);
				}
				return MatrixUtils.createRealVector(data_G);				
			}  	
        };
        
        return func;
    }
}
