import java.util.*;
// convert postfix into NFA
public class NFABuilder {

    static int id = 0;  //counter for the states

    static State newState() {
        return new State(id++);
    }

    public static NFA build(String postfix, String token) {

        Stack<NFA> stack = new Stack<>();

        for (char c : postfix.toCharArray()) {

            if (Character.isLetterOrDigit(c)) {
                State s = newState();
                State e = newState();
                s.addTransition(c, e);
                stack.push(new NFA(s, e));
            }

            else if (c == '.') {
                NFA b = stack.pop();
                NFA a = stack.pop();
                a.end.addEpsilon(b.start);
                stack.push(new NFA(a.start, b.end));
            }

            else if (c == '|') {
                NFA b = stack.pop();
                NFA a = stack.pop();
                State s = newState();
                State e = newState();
                s.addEpsilon(a.start);
                s.addEpsilon(b.start);
                a.end.addEpsilon(e);
                b.end.addEpsilon(e);
                stack.push(new NFA(s, e));
            }

            else if (c == '*') {
                NFA a = stack.pop();
                State s = newState();
                State e = newState();
                s.addEpsilon(a.start);
                s.addEpsilon(e);
                a.end.addEpsilon(a.start);
                a.end.addEpsilon(e);
                stack.push(new NFA(s, e));
            }
        }

        NFA result = stack.pop();
        result.end.tokenName = token;
        return result;
    }
}
