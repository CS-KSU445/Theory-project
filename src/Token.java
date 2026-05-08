

public class Token {

    public String lexeme;
    public String type;
    public int line;
    public int column;

    public Token(String lexeme, String type,
                 int line, int column) {

        this.lexeme = lexeme;
        this.type = type;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {

        return "'" + lexeme + "'  "
                + type
                + "  Line "
                + line
                + ", col "
                + column;
    }
}
