import java.util.*;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import java.awt.Dimension;

class My_Graph 
{ 
	class Edge implements Comparable<Edge> 
	{ 
		int src, dest, weight;  
		public int compareTo(Edge compareEdge) 
		{ 
			return this.weight-compareEdge.weight; 
		} 
	}; 
	
	class subset 
	{ 
		int parent, rank; 
	}; 

	int V, E; 
	Edge edge[];

	// Creates a graph with V vertices and E edges 
	My_Graph(int v, int e) 
	{ 
		V = v; 
		E = e; 
		edge = new Edge[E]; 
		for (int i=0; i<e; ++i) 
			edge[i] = new Edge(); 
	} 

	int find(subset subsets[], int i) 
	{ 
		if (subsets[i].parent != i) 
			subsets[i].parent = find(subsets, subsets[i].parent); 

		return subsets[i].parent; 
	} 

	void Union(subset subsets[], int x, int y) 
	{ 
		int xroot = find(subsets, x); 
		int yroot = find(subsets, y); 

		if (subsets[xroot].rank < subsets[yroot].rank) 
			subsets[xroot].parent = yroot; 
		else if (subsets[xroot].rank > subsets[yroot].rank) 
			subsets[yroot].parent = xroot; 

		else
		{ 
			subsets[yroot].parent = xroot; 
			subsets[xroot].rank++; 
		} 
	} 

	@SuppressWarnings({ "unchecked", "rawtypes" })
	int KruskalMST() 
	{ 
		Edge result[] = new Edge[V]; // Tnis will store the resultant MST 
		int e = 0; // An index variable, used for result[] 
		int i = 0; // An index variable, used for sorted edges 
		for (i=0; i<V; ++i) 
			result[i] = new Edge(); 

		Arrays.sort(edge); 

		subset subsets[] = new subset[V]; 
		
		for(i=0; i<V; ++i) 
			subsets[i]=new subset(); 

		for (int v = 0; v < V; ++v) 
		{ 
			subsets[v].parent = v; 
			subsets[v].rank = 0; 
		} 

		i = 0;

		while (e < V - 1) 
		{ 
			Edge next_edge = new Edge(); 
			next_edge = edge[i++]; 

			int x = find(subsets, next_edge.src); 
			int y = find(subsets, next_edge.dest); 

			if (x != y) 
			{ 
				result[e++] = next_edge; 
				Union(subsets, x, y); 
			} 
		} 
		int sum = 0;
		UndirectedSparseGraph<Integer, Integer> g = new UndirectedSparseGraph<Integer, Integer>();
		for (i = 0; i <V; i++) {
			g.addVertex(i);
		}
		Vector<String> costs = new Vector<String>();
		for (i = 0; i <e; i++) {
			g.addEdge(i , result[i].src, result[i].dest);
			costs.add(String.valueOf(result[i].weight));
			sum += result[i].weight;
		}
		
	    VisualizationImageServer<Integer, Integer> vs =
	            new VisualizationImageServer<Integer, Integer>(new CircleLayout<Integer, Integer>(g), new Dimension(500, 500));

	    vs.getRenderContext().setEdgeLabelTransformer(new org.apache.commons.collections15.Transformer<Integer, String>() {
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
        vs.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

	    
	    vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        //vs.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

	        JFrame frame = new JFrame("MST");
	        frame.getContentPane().add(vs);
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        frame.pack();
	        frame.setLocation(500, 0);
	        frame.setVisible(true);
	        
	        return sum;

	}
} 
