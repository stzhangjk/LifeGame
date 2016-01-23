import java.util.ArrayList;
import java.util.List;

public class Cell {

    private int x;
    private int y;
    private State state;
    private List<Cell> neighbors;

    public enum State{
        LIVE,DEAD
    }

    {
        this.state = State.DEAD;
        neighbors = new ArrayList<>();
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Cell> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Cell> neighbors) {
        this.neighbors = neighbors;
    }
}
