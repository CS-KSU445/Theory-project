import java.util.*;

public class DFAState {

    // مجموعة حالات الـ NFA التي تمثل هذه الحالة في DFA
    public Set<State> nfaStates;

    // transitions الخاصة بالـ DFA
    public Map<Character, DFAState> transitions = new HashMap<>();

    // هل الحالة accepting؟
    public boolean isAccepting = false;

    // اسم التوكن إذا كانت accepting
    public String tokenName = null;

    public DFAState(Set<State> states) {

    this.nfaStates = states;

        // لاختيار أعلى أولوية
    int bestPriority = Integer.MAX_VALUE;

        // فحص جميع حالات NFA داخل DFA state
    for (State s : states) {

            // إذا كانت accepting state
    if (s.tokenName != null) {

                // اختيار أقل priority
     if (s.priority < bestPriority) {

     bestPriority = s.priority;

     this.tokenName = s.tokenName;

     this.isAccepting = true;
  }
}
    }
   }
}
