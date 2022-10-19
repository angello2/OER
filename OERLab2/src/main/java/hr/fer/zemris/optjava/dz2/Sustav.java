package hr.fer.zemris.optjava.dz2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Sustav {
	public static void main(String[] args) throws IOException {
        int maxIter = Integer.parseInt(args[0]);
        String filename = args[1];
        
        System.out.println("Pozvan gradijentni spust nad linearnim sustavom u datoteci: " + filename + " s brojem iteracija " + maxIter);
        
        double[][] data = new double[10][10];
        double[] y_data = new double[10];
        
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
        			if (j == 10) {
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
        final RealMatrix A = MatrixUtils.createRealMatrix(data);
        final RealVector b = MatrixUtils.createRealVector(y_data);
        
        IFunction system = new IFunction() {

			@Override
			public int getNumberOfVariables() {
				return 10;
			}

			@Override
			public double getValue(RealVector x) {
				RealVector linearLeastSquares = A.transpose().preMultiply(x).subtract(b);
				return Math.pow(linearLeastSquares.getNorm(), 2);
			}

			@Override
			public RealVector getGradient(RealVector x) {
				RealVector linearLeastSquares = A.transpose().preMultiply(x).subtract(b);
				return A.preMultiply(linearLeastSquares).mapMultiply(2);
			}        	
        };
        
        RealVector start_x = MatrixUtils.createRealVector(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0});
        
        RealVector min = NumOptAlgorithms.gradientDescent(system, maxIter, start_x, true);
        System.out.println("\nRješenje sustava je [" + min.getEntry(0) + ", " + min.getEntry(1) + ", " + min.getEntry(2) + ", " + 
        		min.getEntry(3) + ", " + min.getEntry(4) + ", " + min.getEntry(5) + ", " + min.getEntry(6) + ", " + min.getEntry(7)
        		+ ", " + min.getEntry(8) + ", " + min.getEntry(9) + "]" + "\nPogreška u trenutku zaustavljanja: " + system.getValue(min));
    }
}
