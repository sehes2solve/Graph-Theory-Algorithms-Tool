import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.awt.*;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class Graph {
	private UndirectedSparseGraph <Integer, Integer> graph ;
	private DirectedSparseGraph<Integer, Integer> euler;
	private Vector<String> Costs_of_intialGraph , Costs_of_HamiltonGraph , edgeList;
	private int edges = 0;
		
	public Graph(int nodes) {
		graph = new UndirectedSparseGraph<Integer, Integer>();
		euler = new DirectedSparseGraph<Integer, Integer>();
		Costs_of_intialGraph 	= new Vector<String>();
		Costs_of_HamiltonGraph 	= new Vector<String>(); 
		edgeList = new Vector<String>();
	}

	public void addEdge(int a, int b) {
		if (!graph.containsVertex(a)) {
			graph.addVertex(a); edgeList.add(String.valueOf(a));
		}

		if (!graph.containsVertex(b)) {
			graph.addVertex(b); edgeList.add(String.valueOf(b));
		}
		graph.addEdge(edges++, a, b);
	}
	
	public void addEdge( int a , int b , int cost ) {
		if ( !graph.containsVertex(a) )
			graph.addVertex(a);
		if ( !graph.containsVertex(b) )
			graph.addVertex(b);
		graph.addEdge(edges++, a, b);
		Costs_of_intialGraph.add( String.valueOf(cost) );
	}
	
	
	public VisualizationImageServer<Integer, Integer> getVisualization() {
		VisualizationImageServer<Integer, Integer> vs =
	            new VisualizationImageServer<Integer, Integer>(new CircleLayout<Integer, Integer>(graph), new Dimension(800, 600));
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
        
        Transformer<Integer, Font> edgeFont = new Transformer<Integer, Font>() {
			@Override
			public Font transform(Integer arg0) {
				return new Font("Arial", Font.PLAIN, 20);
			}
		};
		
		Transformer<Integer, Font> vertexFont = new Transformer<Integer, Font>() {
			@Override
			public Font transform(Integer arg0) {
				return new Font("Arial", Font.BOLD, 20);
			}
		};
       
		vs.getRenderContext().setEdgeFontTransformer(edgeFont);
		vs.getRenderContext().setVertexFontTransformer(vertexFont);
        if ( Costs_of_intialGraph != null ) {
        	vs.getRenderContext().setEdgeLabelTransformer(new org.apache.commons.collections15.Transformer<Integer, String>() {
    			int idx = 0;
    			@Override
    			public String transform(Integer arg0) {
    				if ( idx >= Costs_of_intialGraph.size() ) {
    					idx = 0;
    				}
    				if ( Costs_of_intialGraph == null ) {
    					return "";
    				}
    				if ( Costs_of_intialGraph.isEmpty() ) {
    					return "";
    				}
    				return Costs_of_intialGraph.get(idx++);
    			}
    		});
        }
        return vs;
	}
	
	
	Boolean hasCycle(int v, int[][] visited, int parent, int count) 
    {  
		int m = graph.getEdgeCount();
		if (count == m)
			return false;
		int saveV = visited[v][parent];
		visited[v][parent] = 0;
		visited[parent][v] = 0;
        Collection<Integer> neighbours = graph.getNeighbors(v);
  
        // Recur for all the vertices adjacent to this vertex 
        Iterator<Integer> it = neighbours.iterator() ;//= adj[v].iterator(); 
        Integer num;
        while (it.hasNext()) 
        { 
            num = it.next(); 
            
    		if (visited[num][v] == 1) 
            { 
                if (!hasCycle(num, visited, v, ++count)) 
                    return false; 
            } 
        }
        if (count != m) {
        	visited[v][parent] = visited[parent][v] = saveV;
        	return true;
        }
        return false;
    }
	void dfs(int v,Boolean vis[]) 
    { 
        // Mark the current node as visited and print it 
        vis[v] = true; 
  
        int n; 
  
        // Recur for all the vertices adjacent to this vertex 
        Collection<Integer> a = graph.getNeighbors(v);
		Iterator<Integer> iterator = a.iterator();
        while (iterator.hasNext()) 
        { 
            n = iterator.next(); 
            if (!vis[n]) 
                dfs(n,vis); 
        } 
    }
	public boolean isConnected() {
		int n = graph.getVertexCount();
		Boolean[] vis = new Boolean [n+1];
		
		for (int i=1; i<=n ; i++) 
			vis[i] = false;
		
		dfs(1, vis);
		
		for (int i = 1; i <= n; i++) 
            if (vis[i] == false) 
                return false;
		
		return true;
	}
	
	public VisualizationImageServer<Integer, Integer> eulerPath () {
		int n = graph.getVertexCount();
		int m = graph.getEdgeCount();
		int [][] adj_List = new int [n+1][n+1];
		for (int i=1 ; i<=n;i++) {
			Collection<Integer> a = graph.getNeighbors(i);
			Iterator<Integer> iterator = a.iterator();
			while (iterator.hasNext()){
				int neighbour = iterator.next();
				adj_List[i][neighbour]++;
			}
		}
		
		if (!isConnected())
			return null;
		int odd = 0;
		int source = 1;
		for (int i=1 ; i<=graph.getVertexCount() ; i++)
			if ((graph.degree(i)&1) == 1) {
				odd++; 
				source = i;
			}
		
		if (odd > 2)
			return null;
		
		int numEdges = 0; 
		while (numEdges != m) {
			Collection<Integer> a = graph.getNeighbors(source);
			Iterator<Integer> iterator = a.iterator();
			while (iterator.hasNext()){
				int neighbour = iterator.next();
				int degree = 0;
				if (adj_List[source][neighbour] != 0) {
					for (int i=1 ; i<=n ; i++)
						degree += adj_List[neighbour][i];
					if (degree >= 2 || (degree+numEdges) >= m) {
						int [][] visited = new int [n+1][n+1];
						for (int i=0 ; i<=n ; i++)
							for (int j=0 ; j<=n ; j++)
								visited[i][j] = adj_List[i][j];
						if (!hasCycle(neighbour, visited, source, numEdges+1)) {
							euler.addEdge(numEdges++, source, neighbour);
							adj_List[source][neighbour] = 0;
							adj_List[neighbour][source] = 0;
							source = neighbour;
							break;
						}
					}
				}
			}
		}
		
		VisualizationImageServer<Integer, Integer> vs =
	            new VisualizationImageServer<Integer, Integer>(new CircleLayout<Integer, Integer>(euler), new Dimension(600, 600));

        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
        return vs;
	}
	public VisualizationImageServer<Integer, Integer> eulerCircuit() {
		int n = graph.getVertexCount();
		int m = graph.getEdgeCount();
		int [][] adj_List = new int [n+1][n+1];
		for (int i=1 ; i<=n;i++) {
			Collection<Integer> a = graph.getNeighbors(i);
			Iterator<Integer> iterator = a.iterator();
			while (iterator.hasNext()){
				int neighbour = iterator.next();
				adj_List[i][neighbour]++;
			}
		}
		
		if (!isConnected())
			return null;
		for (int i=1 ; i<=graph.getVertexCount() ; i++)
			if ((graph.degree(i)&1) == 1) 
				return null;
		
		int numEdges = 0;
		int source = 1; 
		while (numEdges != m) {
			Collection<Integer> a = graph.getNeighbors(source);
			Iterator<Integer> iterator = a.iterator();
			while (iterator.hasNext()){
				int neighbour = iterator.next();
				int degree = 0;
				if (adj_List[source][neighbour] != 0) {
					for (int i=1 ; i<=n ; i++)
						degree += adj_List[neighbour][i];
					if (degree >= 2 || (degree+numEdges) >= m) {
						int [][] visited = new int [n+1][n+1];
						for (int i=0 ; i<=n ; i++)
							for (int j=0 ; j<=n ; j++)
								visited[i][j] = adj_List[i][j];
						if (!hasCycle(neighbour, visited, source, numEdges+1)) {
							euler.addEdge(numEdges++, source, neighbour);
							adj_List[source][neighbour] = 0;
							adj_List[neighbour][source] = 0;
							source = neighbour;
							break;
						}
					}
				}
			}
		}
		
		VisualizationImageServer<Integer, Integer> vs =
	            new VisualizationImageServer<Integer, Integer>(new CircleLayout<Integer, Integer>(euler), new Dimension(600, 600));

        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
        return vs;
	}
	
	
	public VisualizationImageServer<Integer, Integer> hamiltonPath(Vector<edge> edgesList) {
		int vertex = graph.getVertexCount();
		Hamilton object = new Hamilton(vertex , edgesList);
		Vector<edge> hamilton = object.hamiltonPath();
		if ( hamilton == null )
			return null;
		DirectedSparseGraph<Integer, Integer> Graph = drawGraph(hamilton);
		return getVisualization_of_hamilton(Graph);
	}
	public VisualizationImageServer<Integer, Integer> hamiltonCircuit(Vector<edge> edgesList) {
		int vertex = graph.getVertexCount();
		Hamilton object = new Hamilton(vertex , edgesList);
		Vector<edge> hamilton = object.hamiltonCircle();
		if ( hamilton == null )
			return null;
		DirectedSparseGraph<Integer, Integer> Graph = drawGraph(hamilton);
		return getVisualization_of_hamilton(Graph);
	}
	
	public VisualizationImageServer<Integer, Integer> minimumHamiltonCircuit(Vector<edge> edgesList) {
		int vertex = graph.getVertexCount();
		Hamilton object = new Hamilton(vertex , edgesList);
		Vector<edge> hamilton = object.minimumHamiltonCircle();
		if ( hamilton == null )
			return null;
		DirectedSparseGraph<Integer, Integer> Graph = drawGraph(hamilton);
		return getVisualization_of_hamilton(Graph);
	}
	
	public VisualizationImageServer<Integer, Integer> getVisualization_of_hamilton(DirectedSparseGraph<Integer, Integer> hamilton){
		VisualizationImageServer<Integer, Integer> vs =
				new VisualizationImageServer<Integer, Integer>( new CircleLayout<Integer, Integer>(hamilton), new Dimension(800,600));
		
		Transformer<Integer, Font> edgeFont = new Transformer<Integer, Font>() {
			@Override
			public Font transform(Integer arg0) {
				return new Font("Arial", Font.PLAIN, 20);
			}
		};
		
		Transformer<Integer, Font> vertexFont = new Transformer<Integer, Font>() {
			@Override
			public Font transform(Integer arg0) {
				return new Font("Arial", Font.BOLD, 20);
			}
		};
       
		vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
		vs.getRenderContext().setEdgeFontTransformer(edgeFont);
		vs.getRenderContext().setVertexFontTransformer(vertexFont);
		vs.getRenderContext().setEdgeLabelTransformer(new org.apache.commons.collections15.Transformer<Integer, String>() {
			int idx = 0;
			@Override
			public String transform(Integer arg0) {
				if ( idx >= Costs_of_HamiltonGraph.size() ) {
					idx = 0;
				}
				if ( Costs_of_HamiltonGraph == null ) {
					return "";
				}
				if ( Costs_of_HamiltonGraph.isEmpty() ) {
					return "";
				}
				return Costs_of_HamiltonGraph.get(idx++);
			}
		});
		vs.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		return vs;
	}
	
	public DirectedSparseGraph<Integer, Integer> drawGraph(Vector<edge> path) {
		DirectedSparseGraph<Integer, Integer> hamilton = new DirectedSparseGraph<Integer, Integer>();
		Costs_of_HamiltonGraph = new Vector<String>();
		for ( int i = 0; i < path.size(); i++ ) {
			Integer from = path.get(i).from - 1;
			Integer to   = path.get(i).to - 1;
			Integer cost = path.get(i).cost;
			
			if ( !hamilton.containsVertex( from ) ) {
				hamilton.addVertex(from);
			}
			if ( !hamilton.containsVertex( to ) ) {
				hamilton.addVertex(to);
			}
			hamilton.addEdge(i, from , to);
			if ( cost == 0 ) {
				Costs_of_HamiltonGraph.add(" ");
			}
			else {
				Costs_of_HamiltonGraph.add(String.valueOf(cost));
			}
		}
		return hamilton;
	}

}
