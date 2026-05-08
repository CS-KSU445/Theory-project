
import java.util.*;

public class DFAState {

    public Set<State> nfaStates;

    public Map<Character, DFAState> transitions = new HashMap<>();

    public boolean isAccepting = false;

    public String tokenName = null;

    public DFAState(Set<State> states) {

        this.nfaStates = states;

        int bestPriority = Integer.MAX_VALUE;

        for (State s : states) {

            if (s.tokenName != null) {

                if (s.priority < bestPriority) {

                    bestPriority = s.priority;

                    this.tokenName = s.tokenName;

                    this.isAccepting = true;
                }
            }
        }
    }
}
