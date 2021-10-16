package hr.fer.zemris.trisat;

import java.util.Optional;
import java.util.Random;

public class Algoritam6 implements IOptAlgorithm {

	private SATFormula formula;
	private Random rand;
	private float postotak = 0.3f;

	public Algoritam6(SATFormula formula) {
		this.formula = formula;
		rand = new Random();
	}
	
	@Override
	public Optional<BitVector> solve(Optional<BitVector> initial) {
		BitVector solutionVector = null;
		Optional<BitVector> solution = Optional.ofNullable(solutionVector);
		
		System.out.println("Pozvan je alternativni iterativni algoritam pretraživanja.");
		System.out.println("Počinjem od nasumično generiranog rješenja " + initial.get() + ".");
		
		MutableBitVector assignment = initial.get().copy();
		
		IOptAlgorithm alg = new Algoritam2(formula);
		
		int i = 0;
		while(i < 100000) {
			System.out.println((i+1) + ". iteracija: Pozivam 2. algoritam na vektoru " + assignment);
			solution = (alg.solve(Optional.ofNullable(assignment)));
			
			if(solution.isPresent()) {
				return solution;
			} else {
				int bitsToChange = (int) (postotak * formula.getNumberOfVariables());
				System.out.println("Algoritam lokalne pretrage nije pronašao rješenje. Mijenjam " + bitsToChange + " bitova.");
				int[] indexesToFlip = new int[bitsToChange];
				for(int j = 0; j < bitsToChange; j++) {
					indexesToFlip[j] = rand.nextInt(formula.getNumberOfVariables());
				}
				MutableBitVector newAssignment = assignment;
				for(int j = 0; j < bitsToChange; j++) {
					System.out.println("Mijenjam " + indexesToFlip[j] + ". bit u vektoru " + newAssignment);
					newAssignment.set(indexesToFlip[j], !newAssignment.get(indexesToFlip[j]));
				}
				assignment = newAssignment;
			}
			
			i++;
		}
		
		return solution;
	}
}