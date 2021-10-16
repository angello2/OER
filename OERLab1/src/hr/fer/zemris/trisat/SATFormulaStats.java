package hr.fer.zemris.trisat;

public class SATFormulaStats {
	private SATFormula formula;
	private boolean isSatisfied = false;
	private BitVector assignment;
	private int numSatisfied = 0;
	private double[] post;
	private double Z;
	
	private double percentageConstantUp = 0.01;
	private double percentageConstantDown = 0.1;
	private double percentageUnitAmount = 50;
	
	
	
	public SATFormulaStats(SATFormula formula) {
		this.formula = formula;
		post = new double[formula.getNumberOfClauses()];
		
		for(int i = 0; i < formula.getNumberOfClauses(); i++) {
			post[i] = 0;
		}
	}
	
	// analizira se predano rješenje i pamte svi relevantni pokazatelji
	// primjerice, ažurira elemente polja post[...] ako drugi argument to dozvoli; računa Z; ...
	public void setAssignment(BitVector assignment, boolean updatePercentages) {
		this.assignment = assignment;
		
		numSatisfied = 0;
		for(int i = 0; i < formula.getNumberOfClauses(); i++) {
			if(formula.getClause(i).isSatisfied(assignment)) {
				numSatisfied++;
				if(updatePercentages) post[i] += (1 - post[i]) * percentageConstantUp; 
			} else if (!formula.getClause(i).isSatisfied(assignment) && updatePercentages) {
				if(updatePercentages) post[i] += (0 - post[i]) * percentageConstantDown;
			}
		}
		
		Z = numSatisfied;
		for(int i = 0; i < formula.getNumberOfClauses(); i++) {
			if(formula.getClause(i).isSatisfied(assignment)) {
				Z += percentageUnitAmount * (1-post[i]);
			} else {
				Z += -percentageUnitAmount * (1-post[i]);
			}
		}
		
		isSatisfied = formula.isSatisfied(assignment);
	}
	
	// vraća temeljem onoga što je setAssignment zapamtio: broj klauzula koje su zadovoljene
	public int getNumberOfSatisfied() {
		return numSatisfied;
	}
	
	// vraća temeljem onoga što je setAssignment zapamtio
	public boolean isSatisfied() {
		return isSatisfied;
	}
	
	// vraća temeljem onoga što je setAssignment zapamtio: suma korekcija klauzula
	// to je korigirani Z iz algoritma 3
	public double getPercentageBonus() {
		return Z;	
	}
	
	// vraća temeljem onoga što je setAssignment zapamtio: procjena postotka za klauzulu
	// to su elementi polja post[...]
	public double getPercentage(int index) {
		return post[index];		
	}
	
	// resetira sve zapamćene vrijednosti na početne (tipa: zapamćene statistike)
	public void reset() {
		for(int i = 0; i < formula.getNumberOfClauses(); i++) {
			post[i] = 0;
		}
		isSatisfied = false;
		numSatisfied = 0;
		Z = 0;
	}
}
