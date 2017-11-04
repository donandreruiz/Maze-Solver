package sp3;
import java.util.HashMap;
import java.util.Map;


public class UnionFind<T> {
	Map<T,T> parent;
	Map<T,Integer> rank;
	
	public UnionFind() {
		parent = new HashMap<T,T>();
		rank = new HashMap<T,Integer>();
	}
	
	public void addSet(T t) {
		parent.put(t, t);
		rank.put(t, 0);
	}

	public T find(T t) {
		T p = parent.get(t);
		if (!t.equals(p))
			parent.put(t, find(p));
		return parent.get(t);
	}
	
	public void union(T t1, T t2) {
		T p1 = this.find(t1);
		T p2 = this.find(t2);
		
		if (p1.equals(p2))
			return;
		
		if (rank.get(p1) < rank.get(p2)) {
			parent.put(p1, p2);
		} else if (rank.get(p1) > rank.get(p2)) {
			parent.put(p2, p1);
		} else {
			rank.put(p1, rank.get(p1) + 1);
			parent.put(p2,p1);
		}
	}
	public String toString() {
		return parent.toString() + "\n" + rank.toString();
	}
}
