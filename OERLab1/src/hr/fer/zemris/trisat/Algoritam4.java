package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class Algoritam4 implements IOptAlgorithm {

	private SATFormula formula;
	private int maxFlips = 100;
	private int maxTries = 100;
	private Random rand = new Random();

	public Algoritam4(SATFormula formula) {
		this.formula = formula;
	}
	
	@Override
	public Optional<BitVector> solve(Optional<BitVector> initial) {
		BitVector solutionVector = null;
		Optional<BitVector> solution = Optional.ofNullable(solutionVector);
		
		System.out.println("Pozvan je GSAT algoritam pretraživanja.");
		
		for(int restart = 1; restart <= maxTries; restart++) {
			BitVector T = new BitVector(rand, formula.getNumberOfVariables());
			
			if(formula.isSatisfied(T)) {
				solution = Optional.ofNullable(T);
				return solution;
			}
			
			System.out.println(restart + ". pokušaj, generiran je random vektor " + T);
			System.out.println("Tražim susjeda koji zadovoljava najviše klauzula.");
			
			for(int promjena = 1; promjena <= maxFlips; promjena++) {	
				
				BitVectorNGenerator gen = new BitVectorNGenerator(T);
				
				int maxNumSatisfied = 0;
				
				for(BitVector n : gen) {

					int numSatisfied = 0;
					for(int i = 0; i < formula.getNumberOfClauses(); i++) {
						if(formula.getClause(i).isSatisfied(n)) numSatisfied++;						
					}
					if(numSatisfied > maxNumSatisfied) maxNumSatisfied = numSatisfied;
				}
				
				ArrayList<BitVector> newVectors = new ArrayList<BitVector>();
				
				for(BitVector n : gen) {
					int numSatisfied = 0;
					for(int i = 0; i < formula.getNumberOfClauses(); i++) {
						if(formula.getClause(i).isSatisfied(n)) numSatisfied++;						
					}
					if(numSatisfied == maxNumSatisfied) {
						newVectors.add(n);						
					}
				}
				
				T = newVectors.get(rand.nextInt(newVectors.size()));
				System.out.println("Pokušaj #" + restart + " Flip #" + promjena + ": Vektor " + T + " zadovoljava " + maxNumSatisfied + " klauzula."); 
				
				if(formula.isSatisfied(T)) {
					solution = Optional.ofNullable(T);
					return solution;
				}
			}
		}		
		return solution;
	}

}