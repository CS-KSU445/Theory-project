// this class convert regex into postfix
import java.util.*;

public class RegexParser {
// check 
    static boolean isOperator(char c) {
        return c == '|' || c == '*' || c == '+' || c == '?' || c == '.';
    }

    static int precedence(char c) {
        if (c == '*' || c == '+' || c == '?') return 3;
        if (c == '.') return 2;
        if (c == '|') return 1;
        return 0;
    }

    static String addConcat(String regex) {     // adding "." for the concat
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < regex.length(); i++) {
            char c1 = regex.charAt(i);
            res.append(c1);

            if (i + 1 < regex.length()) {
                char c2 = regex.charAt(i + 1);

                if ((Character.isLetterOrDigit(c1) || c1 == ')' || c1 == '*' || c1 == '+' || c1 == '?' || c1 == ']') &&
                    (Character.isLetterOrDigit(c2) || c2 == '(' || c2 == '[')) {
                    res.append('.');
                }
            }
        }
        return res.toString();
    }

    public static String toPostfix(String regex) {
        regex = addConcat(regex);

        Stack<Character> stack = new Stack<>();
        StringBuilder output = new StringBuilder();

        for (char c : regex.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                output.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (stack.peek() != '(')
                    output.append(stack.pop());
                stack.pop();
            } else if (isOperator(c)) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c))
                    output.append(stack.pop());
                stack.push(c);
            }
        }

        while (!stack.isEmpty())
            output.append(stack.pop());

        return output.toString();
    }
}
