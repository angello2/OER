package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class Algoritam5 implements IOptAlgorithm {

	private SATFormula formula;
	private int maxFlips = 100;
	private int maxTries = 100;
	private Random rand = new Random();
	private float vjerojatnost = 0.7f;

	public Algoritam5(SATFormula formula) {
		this.formula = formula;
	}
	
	@Override
	public Optional<BitVector> solve(Optional<BitVector> initial) {
		BitVector solutionVector = null;
		Optional<BitVector> solution = Optional.ofNullable(solutionVector);
		
		System.out.println("Pozvan je RandomWalkSAT algoritam pretraživanja.");
		
		for(int restart = 1; restart <= maxTries; restart++) {
			BitVector temp = new BitVector(rand, formula.getNumberOfVariables());
			MutableBitVector T = temp.copy();			
			
			if(formula.isSatisfied(T)) {
				solution = Optional.ofNullable(T);
				return solution;
			}
			
			for(int promjena = 1; promjena <= maxFlips; promjena++) {
				ArrayList<Clause> unsatisfied = new ArrayList<Clause>();
				
				for(int i = 0; i < formula.getNumberOfClauses(); i++) {
					if(!formula.getClause(i).isSatisfied(T)) unsatisfied.add(formula.getClause(i));
				}
				
				Clause randomClause = unsatisfied.get(rand.nextInt(unsatisfied.size()));
				
				if(rand.nextFloat(1.0f) < vjerojatnost) {
					int index = rand.nextInt(randomClause.getSize());
					Integer bit = java.lang.Math.abs(randomClause.getLiteral(index)) - 1;
					T.set(bit, !T.get(bit));
				}
				
				if(rand.nextFloat(1.0f) < (1.0f - vjerojatnost)) {
					
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
					T = (MutableBitVector) newVectors.get(rand.nextInt(newVectors.size()));
				}
				
				System.out.println("Pokušaj " + restart + " Promjena " + promjena + " Trenutni vektor: " + T);
				
				if(formula.isSatisfied(T)) {
					solution = Optional.ofNullable(T);
					return solution;
				}
			}
		}		
		return solution;
	}

}