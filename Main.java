import java.util.Scanner;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Source Language: ");
        String javaCode = sc.nextLine();
        List<String> lexemes = seperateCode(javaCode);
        //System.out.println(lexemes);
        List<String> tokens = tokenize(lexemes);

        /*System.out.print("Tokens: ");
        for (String token : tokens) {
            System.out.print(token + " ");
        }*/

        setPattern(lexemes, tokens);

        sc.close();
    }

    public static void setPattern(List<String> lexemes, List<String> tokens) {

        Pattern pattern;

        String dataType = lexemes.get(tokens.indexOf("<data_type>"));
        String value = lexemes.get(tokens.indexOf("<value>"));
        //System.out.println(dataType);

        switch (dataType) {
            case "int" -> pattern = Pattern.compile("^-?\\d+$");
            case "String" -> pattern = Pattern.compile("^\".*\"$");
            case "char" -> pattern = Pattern.compile("^'[a-zA-Z]'$");
            case "double" -> pattern = Pattern.compile("^-?.?\\d*.?\\d*[\\d+].?$");
            case "float" -> pattern = Pattern.compile("^-?.?\\d*.?\\d*[\\d+].?f$");
            default -> pattern = null;
        }

        assert pattern != null;
        semanticChecker(pattern, value);

    }

    public static void semanticChecker(Pattern pattern, String value) {
        boolean isCorrect = false;
        Matcher matcher;
        matcher = pattern.matcher(value);
        boolean matchFound = matcher.find();
        if (matchFound){
            isCorrect = true;
        }

        if (isCorrect){
            System.out.println("Semantically Correct!");
        } else {
            System.out.println("Semantically Incorrect!");
        }
    }

    public static List<String> tokenize(List<String> lexemes){
        List<String> tokens = new ArrayList<>();
        String[] regEx = {"int|double|float|char|String", "=", ";", "[0-9]+|\"[^\"]{2,}\"|'[a-zA-Z]'", "^[A-Za-z]\\w*$"};
        String[] tokenName = {"data_type", "assignment_operator", "delimiter", "value", "identifier"};

        for (String lexeme : lexemes) {
            boolean isfound = false;
            for(int i=1; i<5; i++){
                Pattern pattern = Pattern.compile(regEx[i]);
                Matcher matcher = pattern.matcher(lexeme);
                boolean found = matcher.find();
                if (found && i == 4){
                    pattern = Pattern.compile(regEx[0]);
                    matcher = pattern.matcher(lexeme);
                    found = matcher.find();
                    isfound = true;
                    if (found){
                        tokens.add("<"+ tokenName[0] + ">");
                    } else {
                        tokens.add("<"+ tokenName[4] + ">");
                    }
                } else if (found) {
                    isfound = true;
                    tokens.add("<" + tokenName[i] + ">");
                }
            }
            if (!isfound){
                tokens.add("<invalid token>");
            }
        }

        return tokens;
    }
    public static List<String> seperateCode(String code) {
        List<String> lexemes = new ArrayList<>();
        Pattern pattern = Pattern.compile("(;)|(=)|(\".*\")|('[a-zA-Z]')|(-?\\.?\\d*\\.?\\d*[\\d+]\\.?f)|([a-zA-Z_$][a-zA-Z0-9_$]*)|(-?\\.?\\d*\\.?\\d*[\\d+]\\.?)");

        Matcher matcher = pattern.matcher(code);
        while (matcher.find()) {
            String lexeme = matcher.group();
            if (!lexeme.matches("\\s*")) {
                lexemes.add(lexeme);
            }
        }

        return lexemes;
    }
}
