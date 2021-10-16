package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class Algoritam2 implements IOptAlgorithm {
	
	private SATFormula formula;

	public Algoritam2(SATFormula formula) {
		this.formula = formula;
	}

	@Override
	public Optional<BitVector> solve(Optional<BitVector> initial) {
		BitVector solutionVector = null;
		Optional<BitVector> solution = Optional.ofNullable(solutionVector);
		
		System.out.println("Pozvan je iterativni algoritam pretraživanja.");
		System.out.println("Počinjem od nasumično generiranog rješenja " + initial.get() + ".");
		
		BitVector assignment = initial.get();
		
		for(int i = 1; i <= 100000; i++) {
			System.out.println("#" + i + ": Vektor " + assignment + " zadovoljava " + fit(assignment) + " klauzula");
			
			if(formula.isSatisfied(assignment)) {
				solution = Optional.ofNullable(assignment);
				break;
			}
			
			BitVectorNGenerator gen = new BitVectorNGenerator(assignment);
			int currentFitness = fit(assignment);
			
			int maxFitness = 0;
			for(BitVector n : gen) {
				if(fit(n) > maxFitness) {
					maxFitness = fit(n);
				}
			}
			
			if(maxFitness <= currentFitness) {
				System.out.println("Algoritam je zapeo u lokalnom optimumu. Izlazim...");
				return solution;
			}
			
			ArrayList<BitVector> mostFitNeighbors = new ArrayList<BitVector>();
			for(BitVector n : gen) {
				if(fit(n) == maxFitness) {
					mostFitNeighbors.add(n);
				}
			}
			
			Random rand = new Random();
			assignment = mostFitNeighbors.get(rand.nextInt(mostFitNeighbors.size()));
		}
		
		return solution;
	}
	
	public int fit(BitVector assignment) {
		int count = 0;
		for(int i = 0; i < formula.getNumberOfClauses(); i++) {
			if(formula.getClause(i).isSatisfied(assignment)) count++;
		}
		return count;
	}
}
