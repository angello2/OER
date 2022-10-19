package hr.fer.zemris.optjava.dz2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Prijenosna {
	public static void main(String[] args) throws IOException {        
        int maxIter = Integer.parseInt(args[0]);
        String filename = args[1];
        
        System.out.println("Pozvan gradijentni spust nad nelinearnim sustavom u datoteci: " + filename + " s brojem iteracija " + maxIter);
        
        double[][] data = new double[20][5];
        double[] y_data = new double[20];
        
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
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
        final RealMatrix x = MatrixUtils.createRealMatrix(data);
        final RealVector y = MatrixUtils.createRealVector(y_data);
        
        IFunction system = new IFunction() {

			@Override
			public int getNumberOfVariables() {
				return 6;
			}

			@Override
			public double getValue(RealVector coefs) {
				// F(x) = 1/2 * G'(x) * G(x)				
				RealVector G = getG(coefs);
				return 0.5 * G.dotProduct(G);
			}

			@Override
			public RealVector getGradient(RealVector coefs) {
				// gradF = Jacobian(x) * G(x)
				double a = coefs.getEntry(0);
				double b = coefs.getEntry(1);
				double c = coefs.getEntry(2);
				double d = coefs.getEntry(3);
				double e = coefs.getEntry(4);
				double f = coefs.getEntry(5);
				
				double[][] data_J = new double[20][6];
				
				for (int i = 0; i < 20; i++) {
					double x1 = x.getEntry(i, 0);					
					double x2 = x.getEntry(i, 1);
					double x3 = x.getEntry(i, 2);
					double x4 = x.getEntry(i, 3);
					double x5 = x.getEntry(i, 4);					
					
					// Jacobian za i-ti red
					double[] row_J = {
							x1,
							Math.pow(x1, 3) * x2,
							Math.exp(d * x3) * (1 + Math.cos(e * x4)),
							c * Math.exp(d * x3) * (1 + Math.cos(e * x4)) * x3,
							c * Math.exp(d * x3) * (-1 * Math.sin(e * x4)) * x4,
							x4 * Math.pow(x5,  2)
					};					
					data_J[i] = row_J;					
				}
				RealMatrix J = MatrixUtils.createRealMatrix(data_J);
				return J.preMultiply(getG(coefs));
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
        
        RealVector start_coefs = MatrixUtils.createRealVector(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0});
        
        RealVector min = NumOptAlgorithms.gradientDescent(system, maxIter, start_coefs);
        System.out.println("\nKoeficijenti sustava su [" + min.getEntry(0) + ", " + min.getEntry(1) + ", " + min.getEntry(2) + ", " + 
        		min.getEntry(3) + ", " + min.getEntry(4) + ", " + "]" + "\nPogreška u trenutku zaustavljanja: " + system.getValue(min));
    }
}
