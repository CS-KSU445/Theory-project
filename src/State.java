import java.util.*;

public class State {
    public int id;
    public Map<Character, List<State>> transitions = new HashMap<>();
    public List<State> epsilon = new ArrayList<>();
    public String tokenName = null;

    public State(int id) {
        this.id = id;
    }

    public void addTransition(char c, State s) {
        transitions.computeIfAbsent(c, k -> new ArrayList<>()).add(s);
    }

    public void addEpsilon(State s) {
        epsilon.add(s);
    }
}
