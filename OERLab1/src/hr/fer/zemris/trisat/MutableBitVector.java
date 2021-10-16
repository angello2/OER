package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Objects;

public class MutableBitVector extends BitVector {
	private ArrayList<Integer> vector = new ArrayList<Integer>();
	
	public MutableBitVector(boolean ... bits) {
		for(boolean bit : bits) {
			if(bit) {
				vector.add(1);
			} else {
				vector.add(0);
			}
		}
	}
	
	public MutableBitVector(int n) {
		for(int i = 0; i < n; i++) {
			vector.add(0);
		}
	}
	
	// zapisuje predanu vrijednost u zadanu varijablu
	@SuppressWarnings("unchecked")
	public void set(int index, boolean value) {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		int i = 0;
		for(Integer bit : vector) {
			if(i != index) {
				temp.add(bit);
			} else {
				if(value) temp.add(1);
				else temp.add(0);
			}
			i++;
		}
		vector.clear();
		vector = (ArrayList<Integer>) temp.clone();
		temp.clear();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Integer bit : vector) {
			sb.append(bit);
		}
		return sb.toString();
	}
	
	public int getSize() {
		return vector.size();
	}
	
	public boolean get(int index) {
		if(vector.get(index) == 1) return true;
		else if(vector.get(index) == 0) return false;
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(vector);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableBitVector other = (MutableBitVector) obj;
		return Objects.equals(vector, other.vector);
	}	
}
