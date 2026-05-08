
import java.util.*;

public class RegexParser {

    public static final char LIT_PLUS = '\uE000';
    public static final char LIT_STAR = '\uE001';
    public static final char LIT_LPAREN = '\uE002';
    public static final char LIT_RPAREN = '\uE003';
    public static final char LIT_DOT = '\uE004';
    public static final char LIT_LBRACE = '\uE005';
    public static final char LIT_RBRACE = '\uE006';

    static boolean isOperator(char c) {
        return c == '|' || c == '*' || c == '+' || c == '?' || c == '.';
    }

    static boolean isRegexParenthesis(char c) {
        return c == '(' || c == ')';
    }

    static boolean isOperand(char c) {
        return !isOperator(c) && !isRegexParenthesis(c);
    }

    static int precedence(char c) {
        if (c == '*' || c == '+' || c == '?') {
            return 3;
        }
        if (c == '.') {
            return 2;
        }
        if (c == '|') {
            return 1;
        }
        return 0;
    }

    public static char restoreLiteral(char c) {
        if (c == LIT_PLUS) return '+';
        if (c == LIT_STAR) return '*';
        if (c == LIT_LPAREN) return '(';
        if (c == LIT_RPAREN) return ')';
        if (c == LIT_DOT) return '.';
        if (c == LIT_LBRACE) return '{';
        if (c == LIT_RBRACE) return '}';
        return c;
    }

    private static char escapedToLiteral(char c) {
        if (c == '+') return LIT_PLUS;
        if (c == '*') return LIT_STAR;
        if (c == '(') return LIT_LPAREN;
        if (c == ')') return LIT_RPAREN;
        if (c == '.') return LIT_DOT;
        if (c == '{') return LIT_LBRACE;
        if (c == '}') return LIT_RBRACE;
        return c;
    }

    private static String expandCharacterClass(String content) {
        List<Character> chars = new ArrayList<>();

        for (int i = 0; i < content.length(); i++) {
            char start = content.charAt(i);

            if (i + 2 < content.length() && content.charAt(i + 1) == '-') {
                char end = content.charAt(i + 2);

                for (char c = start; c <= end; c++) {
                    chars.add(c);
                }

                i += 2;
            } else {
                chars.add(start);
            }
        }

        StringBuilder result = new StringBuilder();
        result.append('(');

        for (int i = 0; i < chars.size(); i++) {
            result.append(chars.get(i));

            if (i != chars.size() - 1) {
                result.append('|');
            }
        }

        result.append(')');
        return result.toString();
    }

    private static String preprocess(String regex) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);

            if (c == '\\') {
                if (i + 1 < regex.length()) {
                    char next = regex.charAt(i + 1);
                    result.append(escapedToLiteral(next));
                    i++;
                }
            }

            else if (c == '[') {
                StringBuilder content = new StringBuilder();
                i++;

                while (i < regex.length() && regex.charAt(i) != ']') {
                    content.append(regex.charAt(i));
                    i++;
                }

                result.append(expandCharacterClass(content.toString()));
            }

            else {
                result.append(c);
            }
        }

        return result.toString();
    }

    static String addConcat(String regex) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < regex.length(); i++) {
            char c1 = regex.charAt(i);
            result.append(c1);

            if (i + 1 < regex.length()) {
                char c2 = regex.charAt(i + 1);

                boolean left =
                        isOperand(c1)
                        || c1 == ')'
                        || c1 == '*'
                        || c1 == '+'
                        || c1 == '?';

                boolean right =
                        isOperand(c2)
                        || c2 == '(';

                if (left && right) {
                    result.append('.');
                }
            }
        }

        return result.toString();
    }

    public static String toPostfix(String regex) {
        regex = preprocess(regex);
        regex = addConcat(regex);

        Stack<Character> stack = new Stack<>();
        StringBuilder output = new StringBuilder();

        for (char c : regex.toCharArray()) {

            if (isOperand(c)) {
                output.append(c);
            }

            else if (c == '(') {
                stack.push(c);
            }

            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop());
                }

                if (!stack.isEmpty()) {
                    stack.pop();
                }
            }

            else if (isOperator(c)) {
                while (!stack.isEmpty()
                        && stack.peek() != '('
                        && precedence(stack.peek()) >= precedence(c)) {

                    output.append(stack.pop());
                }

                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop());
        }

        return output.toString();
    }
}
