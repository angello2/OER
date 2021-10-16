package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class BitVector {
	private ArrayList<Integer> vector = new ArrayList<Integer>();
	
	public BitVector(Random rand, int numberOfBits) {
		for(int i = 0; i < numberOfBits; i++) {
			vector.add(rand.nextInt(2));
		}
	}
	
	public BitVector(boolean ... bits) {
		for(boolean bit : bits) {
			if(bit) {
				vector.add(1);
			} else {
				vector.add(0);
			}
		}
	}
	
	public BitVector(int n) {
		for(int i = 0; i < n; i++) {
			vector.add(0);
		}
	}
	
	// vraća vrijednost index-te varijable
	public boolean get(int index) {
		if(vector.get(index) == 1) return true;
		else if(vector.get(index) == 0) return false;
		return false;
	}
	
	// vraća broj varijabli koje predstavlja
	public int getSize() {
		return vector.size();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Integer bit : vector) {
			sb.append(bit);
		}
		return sb.toString();
	}
	
	// vraća promjenjivu kopiju trenutnog rješenja
	public MutableBitVector copy() {
		boolean[] values = new boolean[vector.size()];
		for(int i = 0; i < vector.size(); i++) {
			if(vector.get(i) == 0) values[i] = false;
			else if(vector.get(i) == 1) values[i] = true;
		}
		MutableBitVector newVector = new MutableBitVector(values);		
		return newVector;
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
		BitVector other = (BitVector) obj;
		return Objects.equals(vector, other.vector);
	}	
}
