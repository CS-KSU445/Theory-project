
import java.util.*;

public class NFABuilder {

    static int id = 0;

    static State newState() {
        return new State(id++);
    }

    private static boolean isOperator(char c) {
        return c == '.' || c == '|' || c == '*' || c == '+' || c == '?';
    }

    private static NFA createLiteralNFA(char c) {
        State s = newState();
        State e = newState();

        s.addTransition(RegexParser.restoreLiteral(c), e);

        return new NFA(s, e);
    }

    public static NFA build(String postfix, String token, int priority) {

        Stack<NFA> stack = new Stack<>();

        for (char c : postfix.toCharArray()) {

            if (!isOperator(c)) {
                stack.push(createLiteralNFA(c));
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

            else if (c == '+') {
                NFA a = stack.pop();

                State s = newState();
                State e = newState();

                s.addEpsilon(a.start);

                a.end.addEpsilon(a.start);
                a.end.addEpsilon(e);

                stack.push(new NFA(s, e));
            }

            else if (c == '?') {
                NFA a = stack.pop();

                State s = newState();
                State e = newState();

                s.addEpsilon(a.start);
                s.addEpsilon(e);

                a.end.addEpsilon(e);

                stack.push(new NFA(s, e));
            }
        }

        NFA result = stack.pop();

        result.end.tokenName = token;
        result.end.priority = priority;

        return result;
    }
}
