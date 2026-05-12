

import java.util.*;

public class TokenSpec {

    public static List<String[]> getSpecs() {

        return List.of(

            // -------------------------
            // Keywords
            // -------------------------

            new String[]{"KW_IF", "if"},
            new String[]{"KW_THEN", "then"},
            new String[]{"KW_ELSE", "else"},
            new String[]{"KW_WHILE", "while"},
            new String[]{"KW_RETURN", "return"},
            new String[]{"KW_FOR", "for"},
            new String[]{"KW_BREAK", "break"},
            new String[]{"KW_CONTINUE", "continue"},
            new String[]{"KW_INT", "int"},
            new String[]{"KW_FLOAT", "float"},


            // -------------------------
            // Identifier & Number
            // -------------------------

            new String[]{"ID",
                    "[A-Za-z][A-Za-z0-9_]*"},

            new String[]{"NUM",
                    "[0-9]+(\\.[0-9]+)?"},


            // -------------------------
            // Operators
            // longest operators first
            // -------------------------

            new String[]{"EQ", "=="},
            new String[]{"LE", "<="},
            new String[]{"GE", ">="},
            new String[]{"NE", "!="},

            new String[]{"ASSIGN", "="},
            new String[]{"LT", "<"},
            new String[]{"GT", ">"},

            new String[]{"PLUS", "\\+"},
            new String[]{"MINUS", "-"},
            new String[]{"MULT", "\\*"},
            new String[]{"DIV", "/"},


            // -------------------------
            // Delimiters
            // -------------------------

            new String[]{"LPAREN", "\\("},
            new String[]{"RPAREN", "\\)"},

            new String[]{"LBRACE", "\\{"},
            new String[]{"RBRACE", "\\}"},

            new String[]{"SEMI", ";"},
            new String[]{"COMMA", ","}
        );
    }
}
