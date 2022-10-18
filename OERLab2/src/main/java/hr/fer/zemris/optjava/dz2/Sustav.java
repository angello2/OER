package hr.fer.zemris.optjava.dz2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

public class Sustav {
	public static void main(String[] args) throws IOException {
        Random rand = new Random();
        
        int maxIter = Integer.parseInt(args[0]);
        String filename = args[1];
        
        System.out.println("Pozvan gradijentni spust nad linearnim sustavom u datoteci: " + filename + " s brojem iteracija " + maxIter +"\n");
        
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
        while (line != null) {
        	System.out.println(line);
        	line = br.readLine();
        }
        br.close();
    }
}
