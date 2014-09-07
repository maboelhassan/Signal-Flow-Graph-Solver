import java.util.ArrayList;

public class MasonSolver {

	private Path path;
	private ArrayList<Loop> loops;
	private ArrayList<Double> gainsofLoops;
	private ArrayList<ArrayList<ArrayList<Integer>>> nonTouchingLoops;

	/*
	 * this relatively big description is for further use . in case we need to
	 * understand the code later on .
	 * 
	 * 
	 * the problem here in this class is that we are dealing with the number of
	 * loops and not the loops themselves . this happened because it was not
	 * clear at the start of the project how to do the calculations . we were
	 * only concerned with finding the loops and paths :)
	 */

	/*
	 * at each position at nonTouchingLoops , there is an arrayList which holds
	 * other array lists . each one of them represents a group of loops which
	 * are non-Touching
	 * 
	 * 
	 * for example : at position 3 (after calling getCombination with size = 3)
	 * 
	 * < < 1 , 2 , 4 > , < 2 , 5 , 6 > , .. .... .. . .. . >
	 * 
	 * this means that at position 3 there will be groups of size 3 and each
	 * group contains the numbers of loops which are non touching to each other
	 * .
	 * 
	 * another example : at position 4
	 * 
	 * < 1 , 2 , 4 , 5 > , < 2 , 5 ,6 ,8 > , ...............
	 * 
	 * at position 4 there are groups of size 4 . each group contains numbers of
	 * loops which are non-touching to each other .
	 */

	private double adjMatrix[][];
	private ArrayList<ArrayList<Pair>> adjList;

	public MasonSolver(int numNodes, ArrayList<ArrayList<Pair>> adj) {
		adjMatrix = new double[numNodes][numNodes];
		for (int i = 0; i < adjMatrix.length; i++)
			for (int j = 0; j < adjMatrix.length; j++)
				adjMatrix[i][j] = 0.0;

		adjList = adj;
		fillAdjMatrix();
	}

	public PathResult solve(Path p, ArrayList<Loop> l) {
		path = p;
		loops = l;
		gainsofLoops = new ArrayList<Double>();
		nonTouchingLoops = new ArrayList<ArrayList<ArrayList<Integer>>>();

		return new PathResult(gainOfPath(), newDeltaOfLoops());
	}

	private double newDeltaOfLoops() {
		int numberOfLoops = loops.size();

		for (int i = 0; i < numberOfLoops; i++) {
			gainsofLoops.add(gainOfLoop(loops.get(i)));
		}

		for (int i = 0; i <= numberOfLoops + 3; i++) {
			nonTouchingLoops.add(new ArrayList<ArrayList<Integer>>());
		}

		for (int i = 1; i <= numberOfLoops; i++) {

			getCombination(0, i, new ArrayList<Integer>());
			if (nonTouchingLoops.get(i).size() == 0)
				break;

		}

		double res = 1.00;

		for (int i = 1; nonTouchingLoops.get(i).size() != 0; i++) {

			double intermediateResult = 0.0;

			for (int j = 0; j < nonTouchingLoops.get(i).size(); j++) {

				ArrayList<Integer> group = nonTouchingLoops.get(i).get(j);
				double tempResult = 1.00;

				for (int k = 0; k < group.size(); k++) {
					tempResult *= gainsofLoops.get(group.get(k));
				}

				intermediateResult += tempResult;
			}

			res += (Math.pow(-1.00, i & 1) * intermediateResult);

		}

		return res;

	}

	private void fillAdjMatrix() {
		for (int i = 0; i < adjMatrix.length; i++) {
			ArrayList<Pair> list = adjList.get(i);
			for (int j = 0; j < list.size(); j++) {
				adjMatrix[i][list.get(j).getToVertex()] = list.get(j)
						.getWeight();
			}
		}
	}

	private boolean checkTouched(ArrayList<Integer> loop1,
			ArrayList<Integer> loop2) {

		boolean touched = false;

		for (int i = 0; i < loop1.size(); i++) {
			for (int j = 0; j < loop2.size(); j++) {

				if (loop1.get(i).equals(loop2.get(j)))
					touched = true;

			}

		}
		return touched;

	}

	private double gainOfPath() {
		double res = 1.00;
		for (int i = 0; i < path.size() - 1; i++) {
			res *= adjMatrix[path.get(i)][path.get(i + 1)];
		}
		return res;
	}

	private double gainOfLoop(Loop loop) {
		double res = 1.00;
		for (int i = 0; i < loop.size() - 1; i++) {
			res *= adjMatrix[loop.get(i)][loop.get(i + 1)];
		}
		res *= adjMatrix[loop.get(loop.size() - 1)][loop.get(0)];

		return res;
	}

	private void handleAdditionOfNonTouchedLoops(ArrayList<Integer> set) {
		int count = 0;
		int n = set.size();
		for (int i = 0; i < set.size() - 1; i++) {
			for (int j = i + 1; j < set.size(); j++) {

				if (!checkTouched(loops.get(set.get(i)), loops.get(set.get(j)))) {
					count++;

				}

			}
		}

		if (count * 2 == n * (n - 1)) {
			if (nonTouchingLoops.size() != 0)
				nonTouchingLoops.get(set.size()).add(set);
		}
	}

	private void getCombination(int k, int size, ArrayList<Integer> set) {

		if (k >= loops.size()) {

			if (set.size() == size)
				handleAdditionOfNonTouchedLoops(set);

			return;

		} else if (set.size() == size) {
			handleAdditionOfNonTouchedLoops(set);
			return;

		} else {

			ArrayList<Integer> copyset = new ArrayList<Integer>();
			for (int j = 0; j < set.size(); j++)
				copyset.add(set.get(j));
			copyset.add(k);
			getCombination(k + 1, size, copyset);
			getCombination(k + 1, size, set);
		}
	}

	public ArrayList<ArrayList<ArrayList<Integer>>> getNonTouchingLoops() {
		return nonTouchingLoops;
	}

}
