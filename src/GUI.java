import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;
import org.jgrapht.graph.UnmodifiableDirectedGraph;
import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private DirectedWeightedPseudograph<String, MyWeightedEdge> DWPgraph;
	private ListenableDirectedWeightedGraph<String, MyWeightedEdge> Lgraph;
	private JGraphModelAdapter<String, MyWeightedEdge> jgAdapter;
	private JGraph jgraph;
	private ControlPanel cp;
	private String source;
	private String sink;

	private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
	private static final Dimension DEFAULT_SIZE = new Dimension(1250, 600);

	public GUI() {
		super("Signal Flow Graph Solver");
		cp = new ControlPanel();
		add(cp, BorderLayout.NORTH);

		DWPgraph = new DirectedWeightedPseudograph<>(MyWeightedEdge.class);
		Lgraph = new ListenableDirectedWeightedGraph<String, MyWeightedEdge>(
				DWPgraph);

		jgAdapter = new JGraphModelAdapter<String, MyWeightedEdge>(Lgraph);

		jgraph = new JGraph(jgAdapter);
		adjustJGraphDisplaySettings();
		add(jgraph, BorderLayout.LINE_START);
		jgraph.setEdgeLabelsMovable(false);
		jgraph.setMoveable(false);
		jgraph.setGridVisible(true);

		// test();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setLocationByPlatform(true);
		setVisible(true);
	}

	private void test() {
		String y1 = "y1";
		String y2 = "y2";
		String y3 = "y3";
		String y4 = "y4";
		String y5 = "y5";

		Lgraph.addVertex(y1);
		Lgraph.addVertex(y2);
		Lgraph.addVertex(y3);
		Lgraph.addVertex(y4);
		Lgraph.addVertex(y5);

		Lgraph.setEdgeWeight(Lgraph.addEdge(y1, y2), 1);
		Lgraph.setEdgeWeight(Lgraph.addEdge(y1, y3), 1);

		Lgraph.setEdgeWeight(Lgraph.addEdge(y2, y3), 3);
		Lgraph.setEdgeWeight(Lgraph.addEdge(y3, y4), 3);

		Lgraph.setEdgeWeight(Lgraph.addEdge(y4, y3), -2);
		Lgraph.setEdgeWeight(Lgraph.addEdge(y4, y2), -2);

		Lgraph.setEdgeWeight(Lgraph.addEdge(y4, y5), 1);

		positionVertexAt(y1, 100, 40);
		positionVertexAt(y2, 200, 200);
		positionVertexAt(y3, 300, 230);
		positionVertexAt(y4, 400, 70);
		positionVertexAt(y5, 1000, 40);

		// String one = "1";
		// String two = "2";
		// String three = "3";
		// String three2 = "4";
		// Lgraph.addVertex(one);
		// Lgraph.addVertex(two);
		// Lgraph.addVertex(three);
		// Lgraph.addVertex(three2);
		// Lgraph.setEdgeWeight(Lgraph.addEdge(two, three), 3.0);
		// positionVertexAt(one, 130, 40);
		// positionVertexAt(two, 60, 200);
		// positionVertexAt(three, 310, 230);
		// positionVertexAt(three2, 380, 70);
	}

	private void adjustJGraphDisplaySettings() {
		jgraph.setPreferredSize(DEFAULT_SIZE);
		jgraph.setEditable(false);
		Color c = DEFAULT_BG_COLOR;
		jgraph.setBackground(c);
		// jgraph.setBackground(Color.darkGray);
	}

	@SuppressWarnings("unchecked")
	private void positionVertexAt(Object vertex, int x, int y) {
		DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
		AttributeMap attr = cell.getAttributes();
		Rectangle2D bounds = GraphConstants.getBounds(attr);

		Rectangle2D newBounds = new Rectangle2D.Double(x, y, bounds.getWidth(),
				bounds.getHeight());

		GraphConstants.setBounds(attr, newBounds);

		AttributeMap cellAttr = new AttributeMap();
		cellAttr.put(cell, attr);
		jgAdapter.edit(cellAttr, null, null, null);
	}

	private class ControlPanel extends JToolBar {

		private static final long serialVersionUID = 1L;

		private Action newNode = new NewNodeAction("New Node");
		private Action clearAll = new ClearAction("Clear All");
		private Action connect = new ConnectAction("Connect Selected");
		private Action delete = new DeleteAction("Delete Selected");
		private Action solve = new SolveAction("Solve!");
		private Action about = new AboutAction("About");
		private Action setMoveable = new SetMoveable("Set Moveable");
		private Action makeLayout = new MakeLayout("Layout");
		private Action setSource = new SetSourceAction("Set Source");
		private Action setSink = new SetSinkAction("Set Sink");
		private JTextField name = new JTextField(5);
		private JTextField weight = new JTextField(5);

		public ControlPanel() {
			this.setLayout(new FlowLayout(FlowLayout.LEFT));
			this.setBackground(Color.lightGray);

			this.add(new JButton(newNode));
			this.add(new JLabel("Name: "));
			this.add(name);
			this.addSeparator();
			this.addSeparator();
			this.addSeparator();

			this.add(new JButton(connect));
			this.add(new JLabel("Weight: "));
			this.add(weight);
			this.addSeparator();
			this.addSeparator();
			this.addSeparator();

			this.add(new JButton(delete));
			this.add(new JButton(clearAll));
			this.addSeparator();
			this.addSeparator();
			this.addSeparator();

			this.add(new JButton(setSource));
			this.add(new JButton(setSink));
			this.add(new JButton(solve));
			this.addSeparator();
			this.addSeparator();
			this.addSeparator();

			this.add(new JButton(setMoveable));
			this.add(new JButton(makeLayout));
			this.addSeparator();
			this.addSeparator();

			this.add(new JButton(about));
			setFloatable(false);
		}

		private class MakeLayout extends AbstractAction {

			private static final long serialVersionUID = 1L;

			public MakeLayout(String name) {
				super(name);
			}

			public void actionPerformed(ActionEvent e) {
				JGraphHierarchicalLayout hir = new JGraphHierarchicalLayout();
				final JGraphFacade graphFacade = new JGraphFacade(jgraph);
				hir.run(graphFacade);
				jgraph.getGraphLayoutCache().edit(
						graphFacade.createNestedMap(false, false));
			}
		}

		private class SetMoveable extends AbstractAction {

			private static final long serialVersionUID = 1L;

			public SetMoveable(String name) {
				super(name);
			}

			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane
						.showConfirmDialog(
								GUI.this,
								"JGraph allows dangling edges to exist, which affects the calculations\nTake great caution when moving parts not to cause this problem\n Do you still want to enable this feature?",
								"TAKE CARE", 1);

				if (option == JOptionPane.OK_OPTION) {
					jgraph.setMoveable(true);
				}
			}
		}

		private class ClearAction extends AbstractAction {

			private static final long serialVersionUID = 1L;

			public ClearAction(String name) {
				super(name);
			}

			public void actionPerformed(ActionEvent e) {
				Object[] vertices = Lgraph.vertexSet().toArray();
				for (Object string : vertices) {
					Lgraph.removeVertex((String) string);
				}
			}
		}

		private class SolveAction extends AbstractAction {
			private static final long serialVersionUID = 1L;

			public SolveAction(String name) {
				super(name);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				GraphAdapter adapter = new GraphAdapter(
						new UnmodifiableDirectedGraph<>(Lgraph), source, sink);

				String res = adapter.solve();

				JFrame resultFrame = new JFrame("Result");
				JTextArea text = new JTextArea();
				text.setText(res);
				text.setEditable(false);
				resultFrame.add(text);
				resultFrame.pack();
				resultFrame.setResizable(false);
				resultFrame.setVisible(true);
			}
		}

		private class SetSourceAction extends AbstractAction {
			private static final long serialVersionUID = 1L;

			public SetSourceAction(String name) {
				super(name);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] selected = jgraph.getSelectionCells();
				if (selected.length == 1) {
					try {
						DefaultGraphCell x = (DefaultGraphCell) selected[0];
						source = x.toString();
					} catch (Exception exx) {
					}
				}
			}
		}

		private class SetSinkAction extends AbstractAction {
			private static final long serialVersionUID = 1L;

			public SetSinkAction(String name) {
				super(name);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] selected = jgraph.getSelectionCells();
				if (selected.length == 1) {
					try {
						DefaultGraphCell x = (DefaultGraphCell) selected[0];
						sink = x.toString();
					} catch (Exception exx) {

					}
				}
			}
		}

		private class AboutAction extends AbstractAction {
			private static final long serialVersionUID = 1L;

			public AboutAction(String name) {
				super(name);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				String about = "Signal Flow Graph Solver\nLinear Control Systems, Spring 2014\n\nProf.Ibraheem Abdelsalam\nEng.Samia Hafez\n\nBy:\n--------\nMohammed Abo El-Hassan\nOmar Yousry Attia";
				JOptionPane.showMessageDialog(GUI.this, about, "About",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}

		private class ConnectAction extends AbstractAction {

			private static final long serialVersionUID = 1L;

			public ConnectAction(String name) {
				super(name);
			}

			public void actionPerformed(ActionEvent e) {
				Object[] selected = jgraph.getSelectionCells();
				if (selected.length == 2 || selected.length == 1) {
					DefaultGraphCell source = (DefaultGraphCell) selected[0];
					DefaultGraphCell dest = (DefaultGraphCell) selected[selected.length == 2 ? 1
							: 0];
					boolean validSelection = Lgraph.containsVertex(source
							.toString())
							&& Lgraph.containsVertex(dest.toString());

					if (validSelection) {
						try {
							double w = Double.parseDouble(weight.getText());
							Lgraph.setEdgeWeight(
									Lgraph.addEdge(source.toString(),
											dest.toString()), w);
						} catch (Exception ex) {

						}
					}
				}
			}
		}

		private class DeleteAction extends AbstractAction {

			private static final long serialVersionUID = 1L;

			public DeleteAction(String name) {
				super(name);
			}

			public void actionPerformed(ActionEvent e) {
				Object[] selected = jgraph.getSelectionCells();
				jgAdapter.remove(selected);
			}
		}

		private class NewNodeAction extends AbstractAction {

			private static final long serialVersionUID = 1L;

			public NewNodeAction(String name) {
				super(name);
			}

			private MouseAdapter mouseAdp = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					int x = e.getX();
					int y = e.getY();
					String t = name.getText().trim();
					if (t != null & t.length() >= 1) {
						Lgraph.addVertex(t);
						positionVertexAt(t, x, y);
					}
					jgraph.removeMouseListener(this);
				}
			};

			public void actionPerformed(ActionEvent e) {
				jgraph.addMouseListener(mouseAdp);
			}
		}
	}
}
