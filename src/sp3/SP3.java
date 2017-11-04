package sp3;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import javax.swing.JFrame;

import sp3.Maze.Position;


public class SP3 {
	static int count = 0;
	
	static Position matchPos;
	
	public static int countCalls(){
		count+=1;
		//System.out.println(count);
		return count;
	}
	
	
	//------------both methods that help Bi and normal BFS gets the path back from the parent map------
	public static List<Position> getPath(Position start, Position goal,
	        HashMap<Position, Position> parentMap, List<Position> path) {
	        // construct output list
	        Position currNode = goal;
	        while(!currNode.equals(start)){
	            path.add(0, currNode);
	            currNode = parentMap.get(currNode);
	            
	        }
	        path.add(0 , start);
	        return path;
	   }
	
	public static List<Position> getPathBi(Position start, Position goal,
        HashMap<Position, Position> parentMap, List<Position> path, int bool, Position goalPos) {
        // construct output list
		if(bool == 2){
			start = goalPos;
			Position currNode = goal;
	        while(!currNode.equals(start)){
	            path.add(currNode);
	            currNode = parentMap.get(currNode);
	        }
	        path.add(start);
		}else if(bool ==1){
		
        Position currNode = goal;
        while(!currNode.equals(start)){
            path.add(0, currNode);
            currNode = parentMap.get(currNode);
        }
        path.add(0 , start);
        
		}
		return path;
   }
	
	
	
	
	//-----------------------------BFS implementation----------------------------------------
	public static void BFS(Position startPos, Position goalPos, List<Position> path, Maze m){
		HashSet<Position> visitedPos = new HashSet<Position>();
		HashMap<Position, Position> myMap = new HashMap<Position, Position>();
		Queue<Position> queue = new LinkedList<>();
		queue.add(startPos);
		visitedPos.add(startPos);
		myMap.put(startPos, null);
		
		while(queue.size() != 0){
			Position p = queue.remove();
			Set<Position> mySet  = m.getNeighboringSpaces(p);
			countCalls();
			for(Position pos: mySet){
				if(!visitedPos.contains(pos)){
					visitedPos.add(pos);
					myMap.put(pos, p);
					queue.add(pos);
					if(pos.equals(goalPos)){
						getPath(startPos, goalPos,
					            myMap, path);
					}
				}
			}
		}
	}
	
	
	
	//-----------------------------DFS implementation----------------------------------------
	public void start_DFS(Position startPos, Position goalPos, List<Position> path, Maze m){
		ArrayList<Position> visitedPos = new ArrayList<Position>();
		DFS(startPos, goalPos, path, m, visitedPos);
		path.add(startPos);
	}
	
	
	
	
	
	
	public int DFS(Position startPos, Position goalPos, List<Position> path, Maze m, ArrayList<Position> visitedPos){
		Queue<Position> queue = new LinkedList<>();
		queue.add(startPos);
		visitedPos.add(startPos);
		if(startPos.equals(goalPos)){
			return 1; 
		}
			Set<Position> mySet  = m.getNeighboringSpaces(startPos);
			countCalls();
			for(Position pos: mySet){
				if(!visitedPos.contains(pos)){
					if(DFS(pos, goalPos, path, m, visitedPos) == 1){
						path.add(pos);
						return 1;
						
					}
				}
			}
			
		
		queue.remove(startPos);
		return 0;
	}
	
	public int HeuFunc(Position a, Position b){
		int dis = (int)Math.sqrt(Math.pow((a.x-b.x), 2) + Math.pow((a.y-b.y), 2));
		return dis;
	}
	
	
	
	
	//-----------------------------A Star implementation----------------------------------------
	public int AStar(Position startPos, Position goalPos, List<Position> path, Maze m){
		PriorityQueue<Entry> pq = new PriorityQueue<Entry>();
		HashMap<Position, Integer> distMap = new HashMap<Position, Integer>();
		HashMap<Position, Position> parentMap = new HashMap<Position, Position>();
		
		Entry startEntry = new Entry(startPos, HeuFunc(startPos, new Position(0,0)));
		pq.add(startEntry);
		distMap.put(startPos, 0);
		parentMap.put(startPos, null);
		while(!pq.isEmpty()){
			Entry v = pq.poll();
			Position vPos = v.getKey();
			if(vPos.equals(goalPos)){
				getPath(startPos, goalPos,
				        parentMap, path);
				return 1;
			}
			Set<Position> mySet  = m.getNeighboringSpaces(v.getKey());
			countCalls();
			for(Position pos: mySet){
				if(distMap.get(pos) == null){
					Entry uEntry = new Entry(pos, HeuFunc(pos, goalPos));
					pq.add(uEntry);
					
					
					distMap.put(pos, Integer.MAX_VALUE);
				}
				if(distMap.get(v.getKey()) + HeuFunc(v.getKey(), pos) < distMap.get(pos)){
					int distPos = distMap.get(pos);
					Entry oldPosEntry = new Entry(pos, distPos);
					distPos = distMap.get(v.getKey()) + HeuFunc(v.getKey(), pos);
					distMap.put(pos, distPos);
					Entry posEntry = new Entry(pos, distMap.get(pos) + HeuFunc(v.getKey(), pos));
					if(pq.contains(oldPosEntry)){
						pq.remove(oldPosEntry);
						pq.add(posEntry);
					}else{
						pq.add(posEntry);
					}
					parentMap.put(pos, v.getKey());
				}
			}
		}
		return 0;
		
	}
	
	
	
	
	
	
	
	
	HashMap<Position, Position> map1 = new HashMap<Position, Position>();
	HashMap<Position, Position> map2 = new HashMap<Position, Position>();
//	-----------------------------BiDirec BFS----------------------------------------
	public boolean bidirectBFS(Position startPos, Position goalPos, List<Position> path, Maze m){
		Queue<Position> queue1 = new LinkedList<>();
		Queue<Position> queue2 = new LinkedList<>();
		ArrayList<Position> visitedPos1 = new ArrayList<Position>();
		ArrayList<Position> visitedPos2 = new ArrayList<Position>();
		
		
		visitedPos1.add(startPos);
		visitedPos2.add(goalPos);
		queue1.add(startPos);
		queue2.add(goalPos);
		map1.put(startPos, null);
		map2.put(goalPos, null);
		
		while(!queue1.isEmpty() && !queue2.isEmpty()){
			if(bidirectBFS_helper(queue1,queue2,  visitedPos1, path, m, map1, startPos, goalPos)){
				List<Position> list1 =  getPathBi(startPos, matchPos, map1, path, 1, goalPos);
				List<Position> list2 = getPathBi(startPos, matchPos, map2, path, 2, goalPos);
				return true;
			}
			
			if(bidirectBFS_helper(queue2, queue1, visitedPos2,path, m, map2, startPos, goalPos)){
				List<Position> list1 =  getPathBi(startPos, matchPos, map1, path, 1, goalPos);
				List<Position> list2 = getPathBi(startPos, matchPos, map2, path, 2, goalPos);
				return true;
			}
		
		}
		
		
		return false;
		
	}
	
	public boolean bidirectBFS_helper(Queue<Position> queue, Queue<Position> queue2, ArrayList<Position> visited, List<Position> path, Maze m, HashMap<Position, Position> map, Position startPosition, Position endPos){
		
			Position n = queue.remove();
			Set<Position> mySet  = m.getNeighboringSpaces(n);
			countCalls();
			for(Position pos: mySet){
				
				if(queue2.contains(pos)){
					map.put(pos, n);
					matched(pos);
					return true;
				}else if(!visited.contains(pos)){
					map.put(pos, n);
					queue.add(pos);
					visited.add(pos);
				}
			}

		return false;
		
	}
	
	public Position matched(Position pos){
		matchPos = pos;
		return matchPos;
	}
	

		
	
	
	public static void main(String[] args) {	
		int dim = 400;
		Maze m = new Maze(dim);

		Position startPos = new Position(0,0);
		Position goalPos = new Position(dim-1,dim-1);
		
		List<Position> path = new ArrayList<Position>();
		
		
		
		
		SP3 sp3Obj = new SP3();
		//----------------BFS----------------------
//		sp3Obj.BFS(startPos, goalPos, path, m);
		
		
		//------------------DFS-----------------------
//		sp3Obj.start_DFS(startPos, goalPos, path, m);
		
		
		//-------------------A*-------------------
		sp3Obj.AStar(startPos, goalPos, path, m);
		
		//---------------Bidirectional BFS-------------------
		//sp3Obj.bidirectBFS(startPos, goalPos, path, m);
		
		System.out.println(count);
		
		
		
		
		
		
		JFrame f = new JFrame();
		f.setTitle("SP3: Maze");
		f.setSize(1000, 1000);
		f.setLocation(50, 50);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		f.getContentPane().add(new MazePanel(m,path));
		f.setVisible(true);
		f.validate();
		f.repaint();
	}
}
