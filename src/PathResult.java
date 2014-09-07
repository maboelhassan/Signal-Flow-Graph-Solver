public class PathResult {
	private double pathGain;
	private double deltaPath;

	public PathResult(double _pathGain, double _deltaPath) {
		pathGain = _pathGain;
		deltaPath = _deltaPath;
	}

	public double getProduct() {
		return pathGain * deltaPath;
	}

	public PathResult() {
		pathGain = 1.0;
		deltaPath = 1.0;
	}

	public double getPathGain() {
		return pathGain;
	}

	public void setPathGain(double pathGain) {
		this.pathGain = pathGain;
	}

	public double getDeltaPath() {
		return deltaPath;
	}

	public void setDeltaPath(double deltaPath) {
		this.deltaPath = deltaPath;
	}
}
