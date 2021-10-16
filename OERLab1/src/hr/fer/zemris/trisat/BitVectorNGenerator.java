package hr.fer.zemris.trisat;

import java.util.Iterator;

public class BitVectorNGenerator implements Iterable<MutableBitVector> {
	private BitVector assignment;
	
	public BitVectorNGenerator(BitVector assignment) {
		this.assignment = assignment;
	}
	
	// Vraća lijeni iterator koji na svaki next() računa sljedećeg susjeda
	@Override
	public Iterator<MutableBitVector> iterator() {
		Iterator<MutableBitVector> it = new Iterator<MutableBitVector>() {
			int size = assignment.getSize(); //velicina vektora ujedno je i kolicina susjeda koje se moze dobiti
			int cnt = 0;
			
            @Override
            public boolean hasNext() {
            	if(cnt < size) return true;
            	return false;
            }

            @Override
            public MutableBitVector next() {
            	if(hasNext()) {
            		boolean[] values = new boolean[size];
            		
            		for(int i = 0; i < size; i++) {
            			if(i == cnt) values[i] = !(assignment.get(i));
            			else values[i] = assignment.get(i);
            		}
            		MutableBitVector newVector = new MutableBitVector(values);
            		cnt++;
            		return newVector;
            	} else {
            		return null;
            	}
            }
        };
        return it;	
	}
	
	// Vraća kompletno susjedstvo kao jedno polje
	public MutableBitVector[] createNeighborhood() {
		int vectorSize = assignment.getSize();
		MutableBitVector[] neighborhood = new MutableBitVector[vectorSize];
		for(int i = 0; i < vectorSize; i++) {
			MutableBitVector neighbor = assignment.copy();
			neighbor.set(i, !assignment.get(i));
			neighborhood[i] = neighbor;
		}
		return neighborhood;
	}	
}
