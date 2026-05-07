import java.util.*;

public class NFAtoDFA {

    // دمج جميع الـ NFAs في NFA واحد
    public static State combineNFAs(List<NFA> nfas) {

        State newStart = new State(-1);

        for (NFA nfa : nfas) {
            newStart.addEpsilon(nfa.start);
        }

        return newStart;
    }

    // حساب epsilon closure
    public static Set<State> epsilonClosure(Set<State> states) {

        Stack<State> stack = new Stack<>();

        Set<State> closure = new HashSet<>(states);

        // إضافة الحالات الابتدائية للـ stack
        for (State s : states) {
            stack.push(s);
        }

        // DFS على epsilon transitions
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

    // move function
    public static Set<State> move(Set<State> states, char c) {

        Set<State> result = new HashSet<>();

        for (State s : states) {

            if (s.transitions.containsKey(c)) {

                result.addAll(s.transitions.get(c));
            }
        }

        return result;
    }

    // تحويل NFA إلى DFA باستخدام subset construction
    public static DFAState convertToDFA(State start) {

        // إنشاء أول state
        Set<State> startSet = new HashSet<>();

        startSet.add(start);

        // حساب epsilon closure للبداية
        Set<State> startClosure = epsilonClosure(startSet);

        DFAState startDFA = new DFAState(startClosure);

        // BFS
        Queue<DFAState> queue = new LinkedList<>();

        List<DFAState> allStates = new ArrayList<>();

        queue.add(startDFA);

        allStates.add(startDFA);

        // بناء DFA
        while (!queue.isEmpty()) {

            DFAState current = queue.poll();

            // استخراج alphabet
            Set<Character> alphabet = new HashSet<>();

            for (State s : current.nfaStates) {

                alphabet.addAll(s.transitions.keySet());
            }

            // إنشاء transitions
            for (char c : alphabet) {

                // move
                Set<State> moveSet = move(current.nfaStates, c);

                // epsilon closure
                Set<State> closure = epsilonClosure(moveSet);

                // تجاهل الحالات الفارغة
                if (closure.isEmpty()) {
                    continue;
                }
DFAState nextState = null;

         // check if DFA state already exists
 for (DFAState d : allStates) {
 if (d.nfaStates.equals(closure)) {
   nextState = d;
    break;
                    } }

                // إذا state جديدة
  if (nextState == null) {
   nextState = new DFAState(closure);
   allStates.add(nextState);
 queue.add(nextState);
                }

                // إضافة transition
                current.transitions.put(c, nextState);
            }
        }

        return startDFA;
    }
}
