

import java.util.*;

public class Lexer {

    private DFAState startState;

    private boolean hasError = false;
    private String errorMessage = "";

    public Lexer() {

    }

    public Lexer(DFAState startState) {
        this.startState = startState;
    }

    public void setStartState(DFAState startState) {
        this.startState = startState;
    }

    public List<Token> tokenize(String input) {

        List<Token> tokens = new ArrayList<>();

        hasError = false;
        errorMessage = "";

        int pos = 0;
        int line = 1;
        int column = 1;

        while (pos < input.length()) {

            char currentChar = input.charAt(pos);

            if (currentChar == ' ') {
                pos++;
                column++;
                continue;
            }

            if (currentChar == '\n') {
                pos++;
                line++;
                column = 1;
                continue;
            }

            if (currentChar == '\r' || currentChar == '\t') {
                pos++;
                column++;
                continue;
            }

            if (startState == null) {
                hasError = true;
                errorMessage = "Lexing Error: DFA start state is not initialized.";
                break;
            }

            int startPos = pos;
            int startLine = line;
            int startColumn = column;

            DFAState currentState = startState;

            DFAState lastAcceptingState = null;
            int lastAcceptingPos = -1;

            int tempPos = pos;

            while (tempPos < input.length()) {

                char c = input.charAt(tempPos);

                if (c == ' ' || c == '\n' || c == '\r' || c == '\t') {
                    break;
                }

                if (!currentState.transitions.containsKey(c)) {
                    break;
                }

                currentState = currentState.transitions.get(c);

                tempPos++;

                if (currentState.isAccepting) {
                    lastAcceptingState = currentState;
                    lastAcceptingPos = tempPos;
                }
            }

            if (lastAcceptingState == null) {

                hasError = true;

                errorMessage =
                        "Lexing Error at Line "
                                + line
                                + ", col "
                                + column
                                + ": '"
                                + currentChar
                                + "'";

                break;
            }

            String lexeme =
                    input.substring(startPos, lastAcceptingPos);

            Token token =
                    new Token(
                            lexeme,
                            lastAcceptingState.tokenName,
                            startLine,
                            startColumn
                    );

            tokens.add(token);

            while (pos < lastAcceptingPos) {

                char consumed = input.charAt(pos);

                if (consumed == '\n') {
                    line++;
                    column = 1;
                } else {
                    column++;
                }

                pos++;
            }
        }

        return tokens;
    }

    public void printTokens(List<Token> tokens) {

        System.out.println("+----------------+----------------+----------------------+");
        System.out.printf("| %-14s | %-14s | %-20s |%n",
                "Lexeme", "Token", "Position");
        System.out.println("+----------------+----------------+----------------------+");

        for (Token t : tokens) {

            String position = "Line " + t.line + ", col " + t.column;

            System.out.printf("| %-14s | %-14s | %-20s |%n",
                    "'" + t.lexeme + "'",
                    t.type,
                    position);
        }

        System.out.println("+----------------+----------------+----------------------+");

        if (hasError) {
            System.out.println();
            System.out.println(errorMessage);
        } else {
            System.out.println();
            System.out.println("Lexing Completed");
        }
    }
}
