package hr.fer.zemris.optjava.dz3;

import java.io.BufferedReader;
import java.util.Random;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class RegresijaSustava {

	public static void main(String[] args) throws IOException, InterruptedException {
        String filename = "prijenosna.txt";
        
        System.out.println("Pozvan algoritam simuliranog kaljenja nad nelinearnim sustavom u datoteci: " + filename);
        
        double[][] data = new double[20][5];
        double[] y_data = new double[20];
        
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
        int i = 0;
        while (line != null) {
        	double[] row = new double[5];
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
				double value = 0.0f;
				double a = coefs.getEntry(0);
				double b = coefs.getEntry(1);
				double c = coefs.getEntry(2);
				double d = coefs.getEntry(3);
				double e = coefs.getEntry(4);
				double f = coefs.getEntry(5);
				for (int i = 0; i < x.getRowDimension(); i++) {
					double x1 = x.getEntry(i, 0);
					double x2 = x.getEntry(i, 1);
					double x3 = x.getEntry(i, 2);
					double x4 = x.getEntry(i, 3);
					double x5 = x.getEntry(i, 4);
					double rowValue = a * x1 + b * Math.pow(x1,  3) * x2 + c * Math.exp(d * x3) * (1 + Math.cos(e * x4)) + f * x4 * Math.pow(x5, 2);
					value += Math.pow((rowValue - y.getEntry(i)), 2);
				}
				// prosjecna pogreska
				return value / x.getRowDimension();
			}
        };
        Random rand = new Random();
        double[] startingData = new double[system.getNumberOfVariables()];
        for (i = 0; i < system.getNumberOfVariables(); i++) {
        	startingData[i] = rand.nextDouble(-3.0f, 3.0f);
        }
        RealVector startingPoint = MatrixUtils.createRealVector(startingData);
        RealVector minimum = SimulatedAnnealing.solve(system, startingPoint, true, true);
        System.out.println("Pronađen minimum: " + minimum);
	}
}
