

import java.util.*;

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

        System.out.println("===== ENTER INPUT STRING =====");
        System.out.print("Input: ");

        String input = scanner.nextLine();

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
