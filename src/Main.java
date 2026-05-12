
import java.util.*;
import java.nio.file.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        List<String[]> specs = TokenSpec.getSpecs();

        List<NFA> nfas = new ArrayList<>();

        System.out.println("===== TOKEN SPECIFICATIONS =====");

        for (int i = 0; i < specs.size(); i++) {

            String tokenName = specs.get(i)[0];
            String regex = specs.get(i)[1];

            int priority = i;

            System.out.println(tokenName + " -> " + regex);

            String postfix = RegexParser.toPostfix(regex);

            NFA nfa = NFABuilder.build(postfix, tokenName, priority);

            nfas.add(nfa);
        }

        System.out.println();

        State combinedStart = NFAtoDFA.combineNFAs(nfas);

        DFAState dfaStart = NFAtoDFA.convertToDFA(combinedStart);

        String input = "";

        System.out.println("===== INPUT METHOD =====");
        System.out.println("1. Enter input manually");
        System.out.println("2. Read input from input.txt");
        System.out.print("Choose option 1 or 2: ");

        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {

            System.out.println();
            System.out.println("===== ENTER INPUT STRING =====");
            System.out.print("Input: ");

            input = scanner.nextLine();

        } else if (choice.equals("2")) {

            String filePath = "input.txt";

            try {
                input = Files.readString(Paths.get(filePath));
            } catch (IOException e) {
                System.out.println("Error: Could not read input.txt.");
                System.out.println("Make sure input.txt is placed in the project folder.");
                scanner.close();
                return;
            }

        } else {

            System.out.println("Invalid choice. Program terminated.");
            scanner.close();
            return;
        }

        System.out.println();
        System.out.println("===== INPUT STRING =====");
        System.out.println(input);
        System.out.println();

        Lexer lexer = new Lexer(dfaStart);

        List<Token> tokens = lexer.tokenize(input);

        System.out.println("===== TOKEN STREAM =====");

        lexer.printTokens(tokens);

        scanner.close();
    }
}
