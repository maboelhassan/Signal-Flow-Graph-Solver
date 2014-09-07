import java.util.ArrayList;
import java.util.Collections;

public class Graph {
	protected ArrayList<ArrayList<Pair>> adjacencyList;
	protected int numberOfNodes;
	protected int source;
	protected int sink;

	private boolean vis[];
	private ArrayList<Loop> currentlyFilledLoops;
	private boolean solved;

	protected ArrayList<Path> allPaths;
	protected ArrayList<ArrayList<Loop>> allLoops;

	public Graph(int _numberOfNodes) {
		numberOfNodes = _numberOfNodes;
		adjacencyList = new ArrayList<ArrayList<Pair>>(numberOfNodes);
		for (int i = 0; i < numberOfNodes; i++)
			adjacencyList.add(new ArrayList<Pair>());

		currentlyFilledLoops = new ArrayList<>();
		allPaths = new ArrayList<>();
		allLoops = new ArrayList<>();
		solved = false;
		vis = new boolean[numberOfNodes];
	}

	public void solveGraph() {
		if (!solved) {
			solvePaths();
			solveLoops();
			solved = true;
		}
	}

	private void solvePaths() {
		// fill the first path (at pos = 0) with an empty ArrayList
		allPaths.add(new Path());

		Path all = new Path();
		all.add(source);
		getPaths(source, all);
	}

	private void getPaths(int current, Path path) {
		if (current == sink) {
			if (checkNotRepeated(path)) {
				allPaths.add(path);
			}
			return;
		} else {
			vis[current] = true;

			for (int i = 0; i < adjacencyList.get(current).size(); i++) {
				int v = adjacencyList.get(current).get(i).getToVertex();
				if (!vis[v]) {
					Path copy = new Path();
					for (int kk = 0; kk < path.size(); kk++)
						copy.add(path.get(kk));
					copy.add(v);
					getPaths(v, copy);
				}

			}
			vis[current] = false;
		}
	}

	private boolean checkNotRepeated(Path path) {
		Collections.sort(path);
		for (int i = 0; i < allPaths.size(); i++) {
			Path c = allPaths.get(i);
			Collections.sort(c);
			if (c.equals(path))
				return false;
		}
		return true;
	}

	private void solveLoops() {
		// while nontouching != false
		for (Path p : allPaths) {
			for (int i = 0; i < numberOfNodes; i++) {
				vis = new boolean[numberOfNodes];
				// add parameter to get loops to indicate path removal
				// path 0, nothing is removed (since it is empty)
				// path 1, path 1 is removed ... etc
				if (!p.contains(i))
					getLoops(i, new Loop(), i, p);
			}

			allLoops.add(currentlyFilledLoops);
			currentlyFilledLoops = new ArrayList<>();
		}
	}

	private void getLoops(int currentNode, Loop loop, int startNode,
			Path excludedPath) {
		if (vis[currentNode] && currentNode == startNode) {
			// loop found
			handleAdditionOfLoops(loop);
			return;
		} else if (vis[currentNode]) {
			return;
		} else {
			vis[currentNode] = true;
			for (int i = 0; i < adjacencyList.get(currentNode).size(); i++) {
				int toVertex = adjacencyList.get(currentNode).get(i)
						.getToVertex();
				// check if this vertex belongs to the path which parameter is
				// already sent, if belongs, just continue to skip this cycle of
				// the loop.
				if (excludedPath.contains(toVertex)) {
					continue;
					// or maybe return ????? just get the hell out of here
				}
				Loop copyLoop = new Loop();
				for (int k = 0; k < loop.size(); k++)
					copyLoop.add(loop.get(k));
				copyLoop.add(toVertex);
				getLoops(toVertex, copyLoop, startNode, excludedPath);
			}
			vis[currentNode] = false;
		}
	}

	private void handleAdditionOfLoops(Loop loop) {
		Collections.sort(loop);
		if (currentlyFilledLoops.size() == 0) {
			currentlyFilledLoops.add(loop);
		} else {
			boolean similar = false;
			for (int i = 0; i < currentlyFilledLoops.size(); i++) {
				Loop current = currentlyFilledLoops.get(i);
				if (current.size() != loop.size())
					continue;
				int count = 0;
				for (int j = 0; j < current.size(); j++) {
					if (current.get(j) == loop.get(j))
						count++;
				}
				if (count == loop.size()) {
					similar = true;
					break;
				}
			}
			if (!similar) {
				currentlyFilledLoops.add(loop);
			}
		}
	}

	/**
	 * if null, then solveGraph was not called
	 * 
	 * @return
	 */
	public ArrayList<Path> getAllPaths() {
		if (solved)
			return allPaths;
		else
			return null;
	}

	/**
	 * if null, then solveGraph was not called
	 * 
	 * @return
	 */
	public ArrayList<ArrayList<Loop>> getAllLoops() {
		if (solved)
			return allLoops;
		else
			return null;
	}
}
