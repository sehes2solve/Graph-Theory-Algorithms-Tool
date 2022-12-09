import java.util.*;
import java.util.LinkedList;

public class GraphII {
	
	int V, E; 
	Edge edge[];  // No. of vertices 
    LinkedList<Integer> adj[]; //Adjacency List 
  
    class Edge
	{ 
		int src, dest;  	 
	};
	
	GraphII(int v, int e) 
	{ 
		V = v; 
		E = e; 
		edge = new Edge[E]; 
		for (int i=0; i<e; ++i) 
			edge[i] = new Edge(); 
		adj = new LinkedList[v]; 
        for (int i=0; i<v; ++i) 
            adj[i] = new LinkedList();
	}
	public void addEdge(int v,int w) 
    { 
        adj[v].add(w); 
        adj[w].add(v); //GraphII is undirected 
    } 
	public int[] Coloring() {
		int[] result = new int[V];
		Arrays.fill(result, -1);
		result[0] = 0;
		//storing available colors
		boolean avail[] = new boolean[V];
		Arrays.fill(avail, true);
		
		for(int i=1; i <V; i++)
		{
			Iterator<Integer> it = adj[i].iterator() ; 
            while (it.hasNext()) 
            { 
                int j = it.next(); 
                if (result[j] != -1) 
                    avail[result[j]] = false; 
            }
         // find the first available color 
            int curent; 
            for (curent = 0; curent < V; curent++){ 
                if (avail[curent]) 
                    break; 
            } 
  
            result[i] = curent; // assign the found color 
  
            // reset the values back to true 
            Arrays.fill(avail, true);
		}

		for (int i=0; i<V;i++) {
			System.out.println("Node " + i + " ====>  Color "+ result[i]);
		}
		return result;
	}
}
