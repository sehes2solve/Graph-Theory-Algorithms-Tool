import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public class Hamilton {
	private boolean[] visited;
	
	private Integer vertex , NumberOfNodes;
	private HashMap<Integer, LinkedList<pair<Integer, Integer>>> adjacencyList;
	private LinkedList<pair<Integer, Integer>> minimumHamiltonPath;
	private LinkedList<Integer> path;
	private Vector<edge> hamilton;
	
	private Integer minimumHamiltonCost = null;
	
	private void add( Integer from , Integer to , Integer cost ) {
		if ( adjacencyList.containsKey(from) ) {
			LinkedList<pair<Integer, Integer>> list = adjacencyList.get(from);
			list.add(new pair<Integer, Integer>(to, cost));
			adjacencyList.replace(from, list);
		}
		else {
			LinkedList<pair<Integer, Integer>> list = new LinkedList<pair<Integer,Integer>>();
			list.add(new pair<Integer, Integer>(to, cost));
			adjacencyList.put(from, list);
		}
	}
	public Hamilton( int _vertex , Vector<edge> edgeList ) {
		vertex 	 = _vertex; 
		hamilton = new Vector<edge>();
		path     = new LinkedList<Integer>();
		visited  = new boolean[vertex + 9];
		NumberOfNodes = 0;
		
		adjacencyList = new HashMap<Integer, LinkedList<pair<Integer,Integer>>>();
		for ( int i = 0; i < edgeList.size(); i++ ) {
			add(edgeList.get(i).from , edgeList.get(i).to, edgeList.get(i).cost);
			add(edgeList.get(i).to , edgeList.get(i).from, edgeList.get(i).cost);
		}
		for ( int itr = 1; itr <= vertex; itr++ ) {
			if ( adjacencyList.containsKey(itr) ) {
				NumberOfNodes++;
			}
		}
		minimumHamiltonPath = new LinkedList<pair<Integer,Integer>>();
	}
	
	
	public Vector<edge> hamiltonPath(){
		for ( int itr = 1; itr <= vertex; itr++ ) {
			if ( adjacencyList.containsKey(itr) == false ) {
				continue;
			}
			LinkedList<Integer> list = new LinkedList<Integer>();
			dfs_hamiltonPath(itr, list);
			if ( dfs_hamiltonPath(itr, list) ) {
				break;
			}
		}
		if ( path.size() > 0 ) {
			Iterator<Integer> itr = path.iterator();
			Integer from = itr.next();
			
			while ( itr.hasNext() ) {
				Integer to = itr.next();
				hamilton.add(new edge(from, to));
				from = to;
			}
		}
		else
			return null;
		return hamilton;
	}
	
	
	public Vector<edge> hamiltonCircle(){	
		for ( int itr = 1; itr <= vertex; itr++ ) {
			if ( adjacencyList.containsKey(itr) == false ) {
				continue;
			}
			LinkedList<Integer> list = new LinkedList<Integer>();
			if ( dfs_hamiltonCircle(itr, itr, list) ) {
				break;
			}
		}
		
		if ( path.size() > 0 ) {
			Iterator<Integer> itr = path.iterator();
			Integer from = itr.next();
			
			while ( itr.hasNext() ) {
				Integer to = itr.next();
				hamilton.add(new edge(from, to));
				from = to;
			}
		}
		else
			hamilton = null;
		return hamilton;
	}
	
	public Vector<edge> minimumHamiltonCircle(){
		for ( int itr = 1; itr <= vertex; itr++ ) {
			if ( adjacencyList.containsKey(itr) == false ) {
				continue;
			}
			LinkedList<pair<Integer, Integer>> list = new LinkedList<pair<Integer,Integer>>();
			dfs_minimumHamiltonCircle(itr, 0 , itr, list);
		}
		
		if ( minimumHamiltonPath.size() > 0 ) {
			Iterator<pair<Integer, Integer>> itr = minimumHamiltonPath.iterator();
			pair<Integer,Integer> from = itr.next();
			
			while ( itr.hasNext() ) {
				pair<Integer,Integer> to = itr.next();
				hamilton.add(new edge(from.key, to.key, from.value));
				from = to;
			}
		}
		else
			hamilton = null;
		return hamilton;
	}
	
	private boolean dfs_hamiltonPath( int node , LinkedList<Integer> _path ) {
		boolean havePath = false;
		visited[node] = true;
		_path.add(node);
		
		if ( _path.size() == NumberOfNodes ) {
			havePath = true;
			path = _path;
			return true;
		}
		
		Iterator<pair<Integer, Integer>> nextNodeItr = adjacencyList.get(node).iterator();
		while ( nextNodeItr.hasNext() ) {
			pair<Integer,Integer> nextNode = nextNodeItr.next();
			if ( visited[nextNode.key] == false ) {
				havePath |= dfs_hamiltonPath(nextNode.key, _path);
				if ( havePath )
					return true;
			}
		}
		if ( havePath == false ) {
			_path.pollLast();
		}
		visited[node] = false;
		return havePath;
	}
	
	
	private boolean dfs_hamiltonCircle(int node , int startNode , LinkedList<Integer> _path) {
		boolean havePath = false;
		visited[node] = true;
		_path.add(node);
		
		
		if ( _path.size() == NumberOfNodes ) {
			Iterator<pair<Integer, Integer>> iter = adjacencyList.get(node).iterator();
			while ( iter.hasNext() ) {
				pair<Integer,Integer> nextNode = iter.next();
				if ( nextNode.key == startNode ) {
					_path.add(startNode);
					havePath = true;
					path = _path;
					return true;
				}
			}
		}
		 
		Iterator<pair<Integer, Integer>> nextNodeItr = adjacencyList.get(node).iterator();
		while ( nextNodeItr.hasNext() ) {
			pair<Integer,Integer> nextNode = nextNodeItr.next();
			if ( visited[nextNode.key] == false ) {
				havePath |= dfs_hamiltonCircle(nextNode.key, startNode , _path);
				if ( havePath )
					return true;
			}
		}
		if ( havePath == false ) {
			_path.pollLast();
		}
		visited[node] = false;
		return havePath;
	}

	private void dfs_minimumHamiltonCircle(int node , int cost , int startNode , LinkedList<pair<Integer, Integer>> localPath) {
		LinkedList<pair<Integer,Integer>> _path = new LinkedList<pair<Integer,Integer>>();
		deepCopy(_path, localPath);
		
		visited[node] = true;
		_path.add(new pair<Integer, Integer>(node , 0));
		
		
		if ( _path.size() == NumberOfNodes ) {
			boolean isCircuit = false; Integer minimum = 0;
			
			Iterator<pair<Integer, Integer>> iter = adjacencyList.get(node).iterator();
			while ( iter.hasNext() ) {
				pair<Integer,Integer> nextNode = iter.next();
				if ( nextNode.key == startNode ) {
					if ( isCircuit == false ) {
						minimum = nextNode.value;
						isCircuit = true;
					}
					else if ( nextNode.value < minimum ) {
						minimum = nextNode.value;
					}
				}
			}
			pair<Integer,Integer> back = _path.peekLast();
			_path.pollLast();
			back.value = minimum;
			_path.add(back);
			
			if ( isCircuit ) {
				cost += minimum;
				if ( minimumHamiltonCost == null ) {
					minimumHamiltonCost = cost;
					_path.add(new pair<Integer, Integer>(startNode, 0));
					deepCopy(minimumHamiltonPath, _path);
				}
				else if ( cost < minimumHamiltonCost ) {
					minimumHamiltonCost = cost;
					_path.add(new pair<Integer, Integer>(startNode, 0));
					deepCopy(minimumHamiltonPath, _path);
				}
			}
		}
		 
		Iterator<pair<Integer, Integer>> nextNodeItr = adjacencyList.get(node).iterator();
		while ( nextNodeItr.hasNext() ) {
			pair<Integer,Integer> nextNode = nextNodeItr.next();
			if ( visited[nextNode.key] == false ) {
				pair<Integer,Integer> back = _path.peekLast();
				_path.pollLast();
				back.value = nextNode.value;
				_path.add(back);
				dfs_minimumHamiltonCircle(nextNode.key, cost + nextNode.value , startNode , _path);
			}
		}

		visited[node] = false;
		return;
	}
	
	private void deepCopy( LinkedList<pair<Integer, Integer>> update , LinkedList<pair<Integer, Integer>> base ) {
		update.clear();
		Iterator<pair<Integer,Integer>> itr = base.iterator();
		while ( itr.hasNext() ) {
			pair<Integer,Integer> current = itr.next();
			update.add(new pair<Integer, Integer>(current.key, current.value));
		}
	}
}
