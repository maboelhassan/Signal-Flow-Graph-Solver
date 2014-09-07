public class Pair {

	private int toVertex;
	private double weight;

	public Pair() {
		toVertex = 0;
		weight = 0;
	}

	public String toString() {
		return toVertex + " : " + weight;
	}

	public Pair(int _toVertex, double _weight) {
		toVertex = _toVertex;
		weight = _weight;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getToVertex() {
		return toVertex;
	}

	public void setToVertex(int toVertex) {
		this.toVertex = toVertex;
	}
}