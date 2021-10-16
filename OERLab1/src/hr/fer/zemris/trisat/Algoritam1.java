package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public class Algoritam1 implements IOptAlgorithm {
	private SATFormula formula;
	private HashSet<BitVector> allVectors = new HashSet<BitVector>();
	
	public Algoritam1(SATFormula formula) {
		this.formula = formula;
	}

	@Override
	public Optional<BitVector> solve(Optional<BitVector> initial) {
		double maxSize = java.lang.Math.pow(2, formula.getNumberOfVariables());
		
		System.out.println("Pozvan algoritam 1. Brute force metodom pronalazim rješenje...");
		System.out.println("Ukupno kombinacija bitvektora ima " + maxSize);
		
		makeAllVectors();
		
		ArrayList<BitVector> solutions = new ArrayList<BitVector>();
		
		System.out.println("Pronadena rjesenja: ");
		for(BitVector assignment : allVectors) {
			if(formula.isSatisfied(assignment)) {
				System.out.println(assignment);
				solutions.add(assignment);
			}
		}
		
	    return Optional.of(solutions.get(0));
	}

	private void makeAllVectors() {
		BitVector vektor = new BitVector(formula.getNumberOfVariables());
		allVectors.add(vektor);
		
		BitVectorNGenerator gen = new BitVectorNGenerator(vektor);
		MutableBitVector[] neighbors = gen.createNeighborhood();
		for(MutableBitVector neighbor : neighbors) {
			allVectors.add(neighbor);
		}
		ArrayList<MutableBitVector> temp;
		
		while(allVectors.size() < java.lang.Math.pow(2, formula.getNumberOfVariables())) {
			temp = new ArrayList<MutableBitVector>();
			for(BitVector x : allVectors) {
				gen = new BitVectorNGenerator(x);
				for(MutableBitVector x2 : gen) {
					if(!allVectors.contains(x2)) temp.add(x2);
				}
			}
			for(BitVector x : temp) {
				allVectors.add(x);
			}	
		}		
	}
}
