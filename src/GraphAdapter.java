import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.UnmodifiableDirectedGraph;

public class GraphAdapter extends Graph {

	private HashMap<Integer, String> ISmapper;
	private HashMap<String, Integer> SImapper;
	private UnmodifiableDirectedGraph<String, MyWeightedEdge> graph;
	private MasonSolver solver;
	private Result result;
	private ArrayList<ArrayList<ArrayList<Integer>>> zero_Non_Touched;

	private void fillGraph() {
		for (Map.Entry<Integer, String> entry : ISmapper.entrySet()) {
			Integer key = entry.getKey();
			String value = entry.getValue();
			Set<MyWeightedEdge> edges = graph.outgoingEdgesOf(value);
			for (MyWeightedEdge e : edges) {
				adjacencyList.get(key).add(
						new Pair(SImapper.get(graph.getEdgeTarget(e)), graph
								.getEdgeWeight(e)));
			}
		}
	}

	public GraphAdapter(UnmodifiableDirectedGraph<String, MyWeightedEdge> g,
			String Source, String Sink) {
		super(g.vertexSet().size());
		graph = g;
		ISmapper = new HashMap<>(super.numberOfNodes);
		SImapper = new HashMap<>(super.numberOfNodes);

		Set<String> vertices = graph.vertexSet();
		int i = 0;
		// Because Sets are not stably sorted
		for (String s : vertices) {
			ISmapper.put(i, s);
			SImapper.put(s, i);
			if (s.equals(Source)) {
				source = i;
			} else if (s.equals(Sink)) {
				sink = i;
			}
			i++;
		}
		fillGraph();
		zero_Non_Touched = new ArrayList<>();
	}

	public String solve() {
		solveGraph();
		solver = new MasonSolver(numberOfNodes, adjacencyList);
		result = new Result(allPaths.size());
		System.out.println("pathes : " + allPaths);
		result.add(solver.solve(allPaths.get(0), allLoops.get(0)));
		zero_Non_Touched = solver.getNonTouchingLoops();

		for (int i = 1; i < allPaths.size(); i++) {
			result.add(solver.solve(allPaths.get(i), allLoops.get(i)));
		}

		return generateRes();
	}

	private String loopPrinter(Loop y) {
		StringBuffer buff = new StringBuffer();
		for (int k = 0; k < y.size(); k++) {
			buff.append(" -> " + ISmapper.get(y.get(k)));
		}
		buff.append(" -> " + ISmapper.get(y.get(0)));
		buff.append("\n");

		return buff.toString();
	}

	private String generateRes() {
		StringBuffer buff = new StringBuffer();

		buff.append("Result:\n-------------\n");

		buff.append("1- Forward Paths:\n-------------------------------\n");

		for (int i = 1; i < result.size(); i++) {
			// State Path's nodes
			buff.append("Path #" + i + " : ");
			for (int j = 0; j < allPaths.get(i).size(); j++) {
				buff.append(" -> " + ISmapper.get(allPaths.get(i).get(j)));
			}
			// State its gain
			buff.append("  : Gain = " + result.get(i).getPathGain()
					+ "        \n");
		}

		buff.append("\n\n2- Deltas:\n----------------\n");
		buff.append("Δ = " + result.get(0).getDeltaPath() + "\n");
		for (int i = 1; i < result.size(); i++) {
			buff.append("Δ_" + i + " = " + result.get(i).getDeltaPath() + "\n");
		}

		buff.append("\n\n3- Loops:\n------------------\n");
		// All loops:
		buff.append("**All Loops:\n-----------------\n");
		ArrayList<Loop> x = allLoops.get(0);
		for (int j = 0; j < x.size(); j++) {
			buff.append("Loop #" + (j + 1) + " : ");
			buff.append(loopPrinter(x.get(j)));
		}
		buff.append("\n");

		buff.append("**Tuples of non-Touching Loops:\n----------------------------------------------\n\n");
		for (int i = 2; i < zero_Non_Touched.size(); i++) {
			ArrayList<ArrayList<Integer>> vv = zero_Non_Touched.get(i);
			if (vv.size() != 0) {
				buff.append(i + "-non Touched:\n--------------\n");
				for (int j = 0; j < vv.size(); j++) {
					ArrayList<Integer> bb = vv.get(j);
					for (int k = 0; k < bb.size(); k++) {
						buff.append(loopPrinter(allLoops.get(0).get(bb.get(k))));
					}
					buff.append("\n");
				}
			}
		}
		buff.append("\n\n4- Over All Transfare Function:\n---------------------------------------------\n");

		double DELTA = result.get(0).getDeltaPath();
		double sum = 0.0;
		for (int i = 1; i < result.size(); i++) {
			sum += result.get(i).getProduct();
		}

		sum /= DELTA;

		buff.append("Output / Input = " + sum);

		return buff.toString();
	}
}
