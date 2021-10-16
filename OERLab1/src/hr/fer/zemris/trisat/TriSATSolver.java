package hr.fer.zemris.trisat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

public class TriSATSolver {
	
	public static void main(String[] args) throws IOException {
		IOptAlgorithm alg = null;
		Optional<BitVector> initial = null;
		Optional<BitVector> solution = null;
		int algNum = Integer.parseInt(args[0]);
		String file = args[1];
		SATFormula formula = ucitajIzDatoteke("C:\\Users\\Filip\\workspace\\OERLab1\\01-3sat\\" + file);
		
		switch(algNum) {
		case 1:
			alg = new Algoritam1(formula);
			solution = alg.solve(initial);
			break;
		case 2:
			alg = new Algoritam2(formula);
			initial = Optional.of(new BitVector(new Random(), formula.getNumberOfVariables()));
			solution = alg.solve(initial);
			break;
		case 3:
			alg = new Algoritam3(formula);
			initial = Optional.of(new BitVector(new Random(), formula.getNumberOfVariables()));
			solution = alg.solve(initial);
			break;
		case 4:
			alg = new Algoritam4(formula);			
			solution = alg.solve(null);
			break;
		case 5:
			alg = new Algoritam5(formula);
			solution = alg.solve(null);
			break;
		case 6:
			alg = new Algoritam6(formula);
			initial = Optional.of(new BitVector(new Random(), formula.getNumberOfVariables()));
			solution = alg.solve(initial);
			break;
		}		
		
		if(solution.isPresent()) System.out.println("Pronađeno rješenje: " + solution.get());
		else System.out.println("Rješenje nije pronađeno.");
	}

	private static SATFormula ucitajIzDatoteke(String string) throws IOException {
	    String file = string;
	    int clauseNum = 0, literalNum = 0;
	     
	    BufferedReader reader = new BufferedReader(new FileReader(file));
	    while(reader.ready()) {
	    	String currentLine = reader.readLine();
	    	if(currentLine.startsWith("c")) {
	    		continue;
	    	} else if(currentLine.startsWith("p")) {
	    		String[] parts = currentLine.split(" ");
	    		literalNum = Integer.parseInt(parts[2]);
	    		clauseNum = Integer.parseInt(parts[3]);
	    		break;
	    	}	    	
	    }
	    Clause[] clauses = new Clause[clauseNum];
	    int i = 0;
	    
	    while(reader.ready()) {
	    	int[] indexes = new int[3];
	    	String currentLine = reader.readLine();
	    	if(currentLine.startsWith("%")) break;
	    	String[] parts = currentLine.split(" ");
			indexes[0] = Integer.parseInt(parts[0]);
			indexes[1] = Integer.parseInt(parts[1]);
			indexes[2] = Integer.parseInt(parts[2]);
			Clause clause = new Clause(indexes);
			clauses[i++] = clause;
	    }	    
	    reader.close();

		SATFormula formula = new SATFormula(literalNum, clauses);
	  
		return formula;
	}

}
