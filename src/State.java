import java.util.*;
//  class for representing the node
public class State {
    public int id;
    public Map<Character, List<State>> transitions = new HashMap<>();
    public List<State> epsilon = new ArrayList<>();   //epslon transition    
    public String tokenName = null;   // to store the name of token if it was accepting state

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
