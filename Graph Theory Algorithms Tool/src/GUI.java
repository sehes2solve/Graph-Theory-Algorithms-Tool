import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Paint;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import javax.swing.JTable;
import javax.swing.JCheckBox;

public class GUI {
	private JFrame frmGraphTool;
	private JTextField nodes;
	private JTextField edges;
	private JTextField from;
	private JTextField to;
	private JTextField cost;
	private JTable table;
	private JCheckBox isCosted;
	
	private JButton createbtn;
	private JButton addEdgebtn;
	private JButton clearbtn;
	private JButton btnHamiltonPath;
	private JButton btnNewButton;
	private JButton btnOriginalGraph;
	private JButton btnMinimumHamiltonCircle;
	private JButton btnEulerPath;
	private JButton btnEulerCircle;
	private JButton btnMinimumSpanningTree;
	private JButton btnGraphColoring;
	private int[] colors;
	
	
	@SuppressWarnings("unused")
	private Integer nodesCnt , edgesCnt , edgesCounter;
	private Vector<edge> edgeList;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmGraphTool.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}
	
	private void getGraph() {
		edgeList = new Vector<edge>();
		
		int rows = table.getModel().getRowCount();
		int cols = table.getModel().getColumnCount();
		
		for ( int i = 0; i < rows; i++ ) {
			Integer from 	= Integer.parseInt(table.getModel().getValueAt(i, 0).toString());
			Integer to 		= Integer.parseInt(table.getModel().getValueAt(i, 1).toString());
			Integer cost;
			
			if ( cols == 3 ) {
				cost = Integer.parseInt(table.getModel().getValueAt(i, 2).toString());
			}
			else {
				cost = 0;
			}
			
			edgeList.add( new edge( from , to , cost ) );
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGraphTool = new JFrame();
		frmGraphTool.setResizable(false);
		frmGraphTool.setTitle("Graph Tool");
		frmGraphTool.setBounds(100, 100, 691, 466);
		frmGraphTool.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGraphTool.getContentPane().setLayout(null);
		
		btnNewButton = new JButton("Hamilton Circle");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					getGraph();
					Graph g = new Graph(nodesCnt);
					for ( int i = 0; i < edgeList.size(); i++ ) {
						g.addEdge(++edgeList.elementAt(i).from , ++edgeList.elementAt(i).to , edgeList.elementAt(i).cost);
					}					
					VisualizationImageServer<Integer, Integer> draw = g.hamiltonCircuit(edgeList);
					if (draw != null) {
						JFrame frame = new JFrame("Hamilton Circuit");
						frame.getContentPane().add(draw);
						frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						frame.pack();
						frame.setLocation(300, 0);
						frame.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(new JFrame(), "There is no Hamilton Circuit is this graph", "Invalid", JOptionPane.DEFAULT_OPTION);
					}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 391, 333);
		frmGraphTool.getContentPane().add(scrollPane);
		btnNewButton.setBounds(10, 394, 132, 23);
		frmGraphTool.getContentPane().add(btnNewButton);
		
		btnHamiltonPath = new JButton("Hamilton Path");
		btnHamiltonPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getGraph();
				Graph g = new Graph(nodesCnt);
				for ( int i = 0; i < edgeList.size(); i++ ) {
					g.addEdge(++edgeList.elementAt(i).from, ++edgeList.elementAt(i).to, edgeList.elementAt(i).cost);
				}						
				VisualizationImageServer<Integer, Integer> draw = g.hamiltonPath(edgeList);
				if (draw != null) {
					JFrame frame = new JFrame("Hamilton Path");
					frame.getContentPane().add(draw);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.pack();
					frame.setLocation(300, 0);
					frame.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(new JFrame(), "There is no Hamilton Path in this graph", "Invalid", JOptionPane.DEFAULT_OPTION);
				}
			}
		});
		btnHamiltonPath.setBounds(10, 360, 132, 23);
		frmGraphTool.getContentPane().add(btnHamiltonPath);
		
		isCosted = new JCheckBox("Enable Cost");
		isCosted.setBounds(415, 82, 109, 23);
		frmGraphTool.getContentPane().add(isCosted);
		
		btnEulerPath = new JButton("Euler Path");
		btnEulerPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getGraph();
				try {
					int nodes = nodesCnt;
					Euler g = new Euler(nodes);
					for (int i=0 ; i<edgeList.size() ; i++) {
						g.addEdge(++edgeList.elementAt(i).from, ++edgeList.elementAt(i).to);
					}
					VisualizationImageServer<Integer, Integer> draw = g.eulerPath();
					if (draw != null) {
						JFrame frame = new JFrame("Euler Path");
						frame.getContentPane().add(draw);
						frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						frame.pack();
						frame.setLocation(300, 0);
						frame.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(new JFrame(), "There is no Euler Path", "Invalid", JOptionPane.DEFAULT_OPTION);
					}
				} catch (Throwable t) {
					JOptionPane.showMessageDialog(new JFrame(), "Parse Data Correctly (Write 1-Based) \nEx:\n1 2\n2 3\n3 1", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnEulerPath.setBounds(351, 360, 132, 23);
		frmGraphTool.getContentPane().add(btnEulerPath);
		
		btnEulerCircle = new JButton("Euler Circle");
		btnEulerCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getGraph();
				try {
					int nodes = nodesCnt;
					Euler g = new Euler(nodes);
					for (int i=0 ; i<edgeList.size() ; i++) {
						g.addEdge(++edgeList.get(i).from, ++edgeList.get(i).to);
					}
					VisualizationImageServer<Integer, Integer> draw = g.eulerCircuit();
					if (draw != null) {
						JFrame frame = new JFrame("Euler Circuit");
						frame.getContentPane().add(draw);
						frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						frame.pack();
						frame.setLocation(300, 0);
						frame.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(new JFrame(), "There is no Euler Circuit", "Invalid", JOptionPane.DEFAULT_OPTION);
					}
				} catch (Throwable t) {
					JOptionPane.showMessageDialog(new JFrame(), "Parse Data Correctly (Write 1-Based)  \nEx:\n1 2\n2 3\n3 1", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnEulerCircle.setBounds(351, 394, 132, 23);
		frmGraphTool.getContentPane().add(btnEulerCircle);
		
		btnOriginalGraph = new JButton("Original Graph");
		btnOriginalGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					getGraph();
					Graph g = new Graph(nodesCnt);
					for (int i=0 ; i < edgeList.size(); i++) {
						g.addEdge(edgeList.elementAt(i).from, edgeList.elementAt(i).to);
					}
					VisualizationImageServer<Integer, Integer> draw = g.getVisualization();
					if (draw != null) {
						JFrame frame = new JFrame("Full Graph");
						frame.getContentPane().add(draw);
						frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						frame.pack();
						frame.setLocation(300, 0);
						frame.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(new JFrame(), "Parse Data Correctly \nEx:\n1 2 \n2 3 \n3 1", "Error", JOptionPane.ERROR_MESSAGE);
					}
			}
		});
		btnOriginalGraph.setBounds(152, 360, 190, 23);
		frmGraphTool.getContentPane().add(btnOriginalGraph);
		
		btnMinimumHamiltonCircle = new JButton("Minimum Hamilton Circle");
		btnMinimumHamiltonCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					getGraph();
					Graph g = new Graph(nodesCnt);
					for ( int i = 0; i < edgeList.size(); i++ ) {
						g.addEdge(++edgeList.elementAt(i).from , ++edgeList.elementAt(i).to , edgeList.elementAt(i).cost);
					}					
					VisualizationImageServer<Integer, Integer> draw = g.minimumHamiltonCircuit(edgeList);
					if (draw != null) {
						JFrame frame = new JFrame("Minimum Hamilton Circuit");
						frame.getContentPane().add(draw);
						frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						frame.pack();
						frame.setLocation(300, 0);
						frame.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(new JFrame(), "There is no Hamilton Circuit is this graph", "Invalid", JOptionPane.DEFAULT_OPTION);
					}
			}
		});
		btnMinimumHamiltonCircle.setBounds(152, 394, 190, 23);
		frmGraphTool.getContentPane().add(btnMinimumHamiltonCircle);
		
		btnMinimumSpanningTree = new JButton("Minimum Spanning Tree");
		btnMinimumSpanningTree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getGraph();
				try {
					int E = edgesCnt;
					int V = nodesCnt;
					My_Graph graph = new My_Graph(V, E); 
					for (int i=0; i< E; i++) {
						graph.edge[i].src = edgeList.elementAt(i).from;
						graph.edge[i].dest = edgeList.elementAt(i).to;
						graph.edge[i].weight = edgeList.elementAt(i).cost;
					}
					
					UndirectedSparseGraph<Integer, Integer> gg = new UndirectedSparseGraph<Integer, Integer>();
					for (int i = 0; i <V; i++) {
						gg.addVertex(i);
					}
					Vector<String> costs = new Vector<String>();
					for (int i = 0; i < E; i++) {
						gg.addEdge(i , graph.edge[i].src, graph.edge[i].dest);
						costs.add(String.valueOf(graph.edge[i].weight));

					}
					
				    VisualizationImageServer<Integer, Integer> vs1 =
				            new VisualizationImageServer<Integer, Integer>(new CircleLayout<Integer, Integer>(gg), new Dimension(500, 500));

				    vs1.getRenderContext().setEdgeLabelTransformer(new org.apache.commons.collections15.Transformer<Integer, String>() {
			            int idx = 0;
			            @SuppressWarnings("unused")
						@Override
			            public String transform(Integer arg0) {
			                if ( idx >= costs.size() ) {
			                    idx = 0;
			                }
			                if ( costs == null ) {
			                    return "";
			                }
			                if ( costs.isEmpty() ) {
			                    return "";
			                }
			                return costs.get(idx++);
			            }
			        });
				    
			        vs1.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
			        vs1.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
			        //vs.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

				        JFrame frame = new JFrame("Full Tree");
				        frame.getContentPane().add(vs1);
				        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				        frame.pack();
				        frame.setVisible(true);
					
					
				        int sum = graph.KruskalMST();
					    final JPanel panel = new JPanel();

					    JOptionPane.showMessageDialog(panel, "MST Cost is " + sum , "MST Cost", JOptionPane.INFORMATION_MESSAGE);

				        //TotalLabel.setText("Total Tree Cost is " + sum);
				        
					}
					catch(Exception e1) {
					    final JPanel panel = new JPanel();

					    JOptionPane.showMessageDialog(panel, "Please Enter Data Correctly", "Error", JOptionPane.ERROR_MESSAGE);

					}
			}
		});
		btnMinimumSpanningTree.setBounds(493, 360, 171, 23);
		frmGraphTool.getContentPane().add(btnMinimumSpanningTree);
		
		btnGraphColoring = new JButton("Graph Coloring");
		btnGraphColoring.addActionListener(new ActionListener() {
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent e) {
				getGraph();
				try {

					GraphII graph = new GraphII(nodesCnt, edgesCnt); 

					for (int i=0; i< edgeList.size(); i++) {
						graph.edge[i].src  = edgeList.elementAt(i).from;
						graph.edge[i].dest = edgeList.elementAt(i).to;
						graph.addEdge(edgeList.elementAt(i).from, edgeList.elementAt(i).to);
					}
					
					UndirectedSparseGraph<Integer, Integer> gg = new UndirectedSparseGraph<Integer, Integer>();
					for (int i = 0; i < nodesCnt; i++) {
						gg.addVertex(i);
					}
					for (int i = 0; i < edgesCnt; i++) {
						gg.addEdge(i , graph.edge[i].src, graph.edge[i].dest);
					}
					
				    VisualizationImageServer<Integer, Integer> vs1 =
				            new VisualizationImageServer<Integer, Integer>(new CircleLayout<Integer, Integer>(gg), new Dimension(250, 250));

			        vs1.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

				        JFrame frame = new JFrame("Full Tree");
				        frame.getContentPane().add(vs1);
				        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				        frame.pack();
				        frame.setVisible(true);
					
					    colors = graph.Coloring();

						UndirectedSparseGraph<Integer, Integer> g = new UndirectedSparseGraph<Integer, Integer>();
						for (int i = 0; i < nodesCnt; i++) {
							g.addVertex(i);
						}

						for (int i = 0; i < edgesCnt; i++) {
							g.addEdge(i , graph.edge[i].src, graph.edge[i].dest);
						}
						VisualizationImageServer<Integer, Integer> vs =
						new VisualizationImageServer<Integer, Integer>(new CircleLayout<Integer, Integer>(g), new Dimension(250, 250));


	                    Transformer<Integer, Paint> ColorTransformer = new Transformer<Integer,Paint>() {
	                        public Paint transform(Integer i) {
	                        	switch (colors[i]){
									case 0 : return Color.RED;
									case 1 : return Color.GREEN;
									case 2 : return Color.BLUE;
									case 3 : return Color.YELLOW;
									default : return Color.RED;
								}

	                        }
	                    };

						vs.getRenderContext().setVertexFillPaintTransformer(ColorTransformer);
						vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

						JFrame color_frame = new JFrame("Colored Tree");
						color_frame.getContentPane().add(vs);
						color_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						color_frame.pack();
						color_frame.setLocation(300,0);
						color_frame.setVisible(true);
					}
					catch(Exception e1) {
					    final JPanel panel = new JPanel();
					    JOptionPane.showMessageDialog(panel, "Please Enter Data Correctly", "Error", JOptionPane.ERROR_MESSAGE);

					}
			}
		});
		btnGraphColoring.setBounds(493, 394, 171, 23);
		frmGraphTool.getContentPane().add(btnGraphColoring);
		
		JLabel lblNumberOfEdges = new JLabel("Number of vertices");
		lblNumberOfEdges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNumberOfEdges.setBounds(418, 14, 134, 14);
		frmGraphTool.getContentPane().add(lblNumberOfEdges);
		
		nodes = new JTextField();
		nodes.setBounds(553, 11, 111, 20);
		frmGraphTool.getContentPane().add(nodes);
		nodes.setColumns(10);
		
		JLabel label = new JLabel("Number of edges");
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label.setBounds(418, 42, 134, 18);
		frmGraphTool.getContentPane().add(label);
		
		edges = new JTextField();
		edges.setColumns(10);
		edges.setBounds(553, 39, 111, 20);
		frmGraphTool.getContentPane().add(edges);
		
		createbtn = new JButton("Create Graph");
		createbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					edgesCounter = 0;
					nodesCnt = Integer.parseInt(nodes.getText());
					edgesCnt = Integer.parseInt(edges.getText());
					
					nodes.setEnabled(false);
					edges.setEnabled(false);
					createbtn.setEnabled(false);
					isCosted.setEnabled(false);
					
					if ( !isCosted.isSelected() ) {
						btnMinimumSpanningTree.setEnabled(false);
						btnMinimumHamiltonCircle.setEnabled(false);
					}
					else {
						btnMinimumSpanningTree.setEnabled(true);
						btnMinimumHamiltonCircle.setEnabled(true);	
					}
					
					
					
					
					addEdgebtn.setEnabled(true);
					clearbtn.setEnabled(true);
					from.setEnabled(true);
					to.setEnabled(true);
					cost.setEnabled(true);			
										
					if ( isCosted.isSelected() ) {
						table = new JTable();
						table.setModel(new DefaultTableModel(
							new Object[][] {
							},
							new String[] {
								"Source", "Destination", "Cost"
							}
						));
						
						DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
						centerRenderer.setHorizontalAlignment( JLabel.CENTER );
						table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
						table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
						table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
						scrollPane.setViewportView(table);
					}
					else {
						cost.setEnabled(false);
						table = new JTable();
						table.setModel(new DefaultTableModel(
							new Object[][] {
							},
							new String[] {
								"Source", "Destination"
							}
						));
						
						DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
						centerRenderer.setHorizontalAlignment( JLabel.CENTER );
						table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
						table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
						scrollPane.setViewportView(table);
					}
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(new JFrame(), "Number of nodes & number of edges must be numbers only", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		createbtn.setBounds(553, 78, 111, 30);
		frmGraphTool.getContentPane().add(createbtn);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 347, 654, 2);
		frmGraphTool.getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(405, 11, 1, 333);
		frmGraphTool.getContentPane().add(separator_1);
		
		JLabel lblSource = new JLabel("Source Vertex");
		lblSource.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSource.setBounds(418, 139, 134, 14);
		frmGraphTool.getContentPane().add(lblSource);
		
		from = new JTextField();
		from.setColumns(10);
		from.setBounds(553, 136, 111, 20);
		frmGraphTool.getContentPane().add(from);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(407, 119, 257, 2);
		frmGraphTool.getContentPane().add(separator_2);
		
		JLabel lblDestinationVertex = new JLabel("Destination Vertex");
		lblDestinationVertex.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDestinationVertex.setBounds(418, 167, 134, 14);
		frmGraphTool.getContentPane().add(lblDestinationVertex);
		
		to = new JTextField();
		to.setColumns(10);
		to.setBounds(553, 164, 111, 20);
		frmGraphTool.getContentPane().add(to);
		
		JLabel lblEdgeCost = new JLabel("Edge Cost");
		lblEdgeCost.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEdgeCost.setBounds(418, 198, 134, 17);
		frmGraphTool.getContentPane().add(lblEdgeCost);
		
		cost = new JTextField();
		cost.setColumns(10);
		cost.setBounds(553, 195, 111, 20);
		frmGraphTool.getContentPane().add(cost);
		
		addEdgebtn = new JButton("Add Edge");
		addEdgebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Integer Source 		= Integer.parseInt(from.getText());
					Integer Destination = Integer.parseInt(to.getText());
					
					if ( Source < 0 || Source >= nodesCnt ) {
						JOptionPane.showMessageDialog(new JFrame(), "Source must be in range [0 : Number of nodes - 1]", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if ( Destination < 0 || Destination >= nodesCnt ) {
						JOptionPane.showMessageDialog(new JFrame(), "Destination must be in range [0 : Number of nodes - 1]", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					edgesCounter++;
					if ( edgesCounter + 1 > edgesCnt ) {
						addEdgebtn.setEnabled(false);
					}
					
					String CostStr = "0";
					if ( !(cost.getText() == null || cost.getText().isEmpty()) ) {
						CostStr = cost.getText();
					}
					
					//edgeList.add(new edge(Source, Destination, Cost));
					
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					model.addRow(new String[] {from.getText() , to.getText() , CostStr});
					 
					from.setText(""); to.setText(""); cost.setText("");
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(new JFrame(), "Source, Destination & cost must be numbers only", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		addEdgebtn.setBounds(418, 240, 246, 30);
		frmGraphTool.getContentPane().add(addEdgebtn);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(407, 287, 257, 2);
		frmGraphTool.getContentPane().add(separator_3);
		
		clearbtn = new JButton("Clear This Graph");
		clearbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				from.setEnabled(false);
				to.setEnabled(false);
				cost.setEnabled(false);
				addEdgebtn.setEnabled(false);
				clearbtn.setEnabled(false);
				
				nodes.setEnabled(true);
				edges.setEnabled(true);
				createbtn.setEnabled(true);
				isCosted.setEnabled(true);
				btnMinimumSpanningTree.setEnabled(true);
				btnMinimumHamiltonCircle.setEnabled(true);	
				
				if ( isCosted.isSelected() ) {
					table.setModel(new DefaultTableModel(
						new Object[][] {
						},
						new String[] {
						}
					));	
				}
				else {
					table.setModel(new DefaultTableModel(
						new Object[][] {
						},
						new String[] {
						}
					));	
				}
				
							
				
				from.setText("");
				to.setText("");
				cost.setText("");
				nodes.setText("");
				edges.setText("");
			}
		});
		clearbtn.setBounds(418, 300, 246, 36);
		frmGraphTool.getContentPane().add(clearbtn);
		
		
		
		this.from.setEnabled(false);
		this.to.setEnabled(false);
		this.cost.setEnabled(false);
		this.addEdgebtn.setEnabled(false);
		this.clearbtn.setEnabled(false);
		
		
	}
}
