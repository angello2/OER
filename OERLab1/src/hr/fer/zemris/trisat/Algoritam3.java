package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class Algoritam3 implements IOptAlgorithm {
	private SATFormulaStats stats;
	private int numberOfBest = 2;

	public Algoritam3(SATFormula formula) {
		stats = new SATFormulaStats(formula);
	}
	
	@Override
	public Optional<BitVector> solve(Optional<BitVector> initial) {
		BitVector solutionVector = null;
		Optional<BitVector> solution = Optional.ofNullable(solutionVector);
		
		System.out.println("Pozvan je nadograđeni iterativni algoritam pretraživanja.");
		System.out.println("Počinjem od nasumično generiranog rješenja " + initial.get() + ".");
		
		BitVector assignment = initial.get();
		
		for(int i = 1; i <= 100000; i++) {
			stats.setAssignment(assignment, true);
			System.out.println("#" + i + ": Vektor " + assignment + " zadovoljava " + stats.getNumberOfSatisfied() + " klauzula. Z = " + stats.getPercentageBonus());
			
			if(stats.isSatisfied()) {
				solution = Optional.ofNullable(assignment);
				break;
			}			
			
			HashMap<BitVector, Double> neighbors = new HashMap<BitVector, Double>();
			
			BitVectorNGenerator gen = new BitVectorNGenerator(assignment);
			for(BitVector n : gen) {
				stats.setAssignment(n, false);
				neighbors.put(n, stats.getPercentageBonus());			
			}
			
			Map<BitVector, Double> sortedNeighbors = sortByValues(neighbors);
			
			Set<BitVector> sortedNeighborsSet = sortedNeighbors.keySet();
			
			ArrayList<BitVector> list = new ArrayList<BitVector>(sortedNeighborsSet);
			
			Random rand = new Random();
			assignment = list.get(rand.nextInt(numberOfBest));
		}		
		return solution;
	}
	
	public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
	    Comparator<K> valueComparator =  new Comparator<K>() {
	        public int compare(K k1, K k2) {
	            int compare = map.get(k2).compareTo(map.get(k1));
	            if (compare == 0) return 1;
	            else return compare;
	        }
	    };
	    Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
	    sortedByValues.putAll(map);
	    return sortedByValues;
	}
}
