package hr.fer.zemris.optjava.dz2;

import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

public class Jednostavno {

	public static void main(String[] args) {
        Random rand = new Random();
        
        double x1, x2;

        if (args.length == 4){
            x1 = Double.parseDouble(args[2]);
            x2 = Double.parseDouble(args[3]);
        } else {
            x1 = rand.nextDouble(-5, 5);
            x2 = rand.nextDouble(-5, 5);
        }

        int maxIter = Integer.parseInt(args[1]);
        IFunction function;        
        double [] vectorData = {x1, x2};        
        RealVector startValues = MatrixUtils.createRealVector(vectorData);
        
        System.out.println("Program pozvan s funkcijom " + args[0] + ", maks. brojem iteracija " + args[1] + " i pocetnim rjesenjem: [" + startValues.getEntry(0) + ", " + startValues.getEntry(1) + "]");
        
        RealVector minimum;
        
        switch (args[0]){
            case "1":
                function = new Function1();
                minimum = NumOptAlgorithms.gradientDescent(function, maxIter, startValues);
                System.out.println("Minimum funkcije 1 je [" + minimum.getEntry(0) + ", " + minimum.getEntry(1) + "]");
                break;
            case "2":
                function = new Function2();
                minimum = NumOptAlgorithms.gradientDescent(function, maxIter, startValues);
                System.out.println("Minimum funkcije 2 je [" + minimum.getEntry(0) + ", " + minimum.getEntry(1) + "]");
                break;
        }
    }
}
