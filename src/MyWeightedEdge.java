import org.jgrapht.graph.DefaultWeightedEdge;

public class MyWeightedEdge extends DefaultWeightedEdge {
	private static final long serialVersionUID = 1L;

	public MyWeightedEdge() {
		super();
	}

	@Override
	public String toString() {
		return Double.toString(getWeight());
	}
}