
import java.util.*;

public class NFAtoDFA {

    public static State combineNFAs(List<NFA> nfas) {

        State newStart = new State(-1);

        for (NFA nfa : nfas) {
            newStart.addEpsilon(nfa.start);
        }

        return newStart;
    }

    public static Set<State> epsilonClosure(Set<State> states) {

        Stack<State> stack = new Stack<>();

        Set<State> closure = new HashSet<>(states);

        for (State s : states) {
            stack.push(s);
        }

        while (!stack.isEmpty()) {

            State current = stack.pop();

            for (State next : current.epsilon) {

                if (!closure.contains(next)) {

                    closure.add(next);

                    stack.push(next);
                }
            }
        }

        return closure;
    }

    public static Set<State> move(Set<State> states, char c) {

        Set<State> result = new HashSet<>();

        for (State s : states) {

            if (s.transitions.containsKey(c)) {

                result.addAll(s.transitions.get(c));
            }
        }

        return result;
    }

    public static DFAState convertToDFA(State start) {

        Set<State> startSet = new HashSet<>();

        startSet.add(start);

        Set<State> startClosure = epsilonClosure(startSet);

        DFAState startDFA = new DFAState(startClosure);

        Queue<DFAState> queue = new LinkedList<>();

        List<DFAState> allStates = new ArrayList<>();

        queue.add(startDFA);

        allStates.add(startDFA);

        while (!queue.isEmpty()) {

            DFAState current = queue.poll();

            Set<Character> alphabet = new HashSet<>();

            for (State s : current.nfaStates) {

                alphabet.addAll(s.transitions.keySet());
            }

            for (char c : alphabet) {

                Set<State> moveSet = move(current.nfaStates, c);

                Set<State> closure = epsilonClosure(moveSet);

                if (closure.isEmpty()) {
                    continue;
                }

                DFAState nextState = null;

                for (DFAState d : allStates) {

                    if (d.nfaStates.equals(closure)) {

                        nextState = d;

                        break;
                    }
                }

                if (nextState == null) {

                    nextState = new DFAState(closure);

                    allStates.add(nextState);

                    queue.add(nextState);
                }

                current.transitions.put(c, nextState);
            }
        }

        return startDFA;
    }
}
