package hr.fer.zemris.trisat;

public class Clause {
	private int[] indexes;
	
	public Clause(int[] indexes) {
		this.indexes = indexes;
	}
	
	public int getSize() {
		return indexes.length;
	}
	public int getLiteral(int index) {
		return indexes[index];
	}
	
	public boolean isSatisfied(BitVector assignment) {
		for(int i = 0; i < 3; i++) {
			int index = indexes[i];
			boolean bit = assignment.get(java.lang.Math.abs(index) - 1);
			if(index < 0 && !bit) return true; //dovoljno je da je jedan od literala zadovoljen da cijela klauzula bude
			else if(index > 0 && bit) return true;
		}
		return false;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(indexes[0] < 0) sb.append("(~x" + indexes[0] * (-1));
		else sb.append("(x" + indexes[0]);
		for(int literal : indexes) {
			if(literal == indexes[0]) {
				continue;
			}
			sb.append(" + ");
			if(literal < 0) {
				sb.append("~x" + literal * (-1));
			}
			else {
				sb.append("x" + literal);
			}
		}
		sb.append(")\n");		
		return sb.toString();
	}
}
