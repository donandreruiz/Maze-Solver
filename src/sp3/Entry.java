package sp3;
import sp3.Maze.Position;

public class Entry implements Comparable<Entry>{
	private Position key;
    private int value;

    public Entry(Position key, int value) {
        this.key = key;
        this.value = value;
    }

    public Position getKey(){
    	return this.key;
    }
    
    public int getValue(){
    	return this.value;
    }
    

    public int compareTo(Entry other) {
    	Integer firstInt = this.getValue();
    	Integer secondInt = other.getValue();
        return firstInt.compareTo(secondInt);
    }
}
