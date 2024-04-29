package encryptdecrypt;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Constants {
    static final int UPPERCASE = 2;

    static final int LOWERCASE = 1;

    static final int ALPHABET_LENGTH = 26;

    static final int INT_a = 97;

    static final int INT_z = 122;

    static int INT_A = 65;

    static final int INT_Z = 90;
}

public class Main {

    static String mode = "end";

    static String key = "0";

    static String data = "";

    static String in = "";

    static String out = "";

    static String alg = "shift";

    public static void main(String[] args) {
        loadArguments(args);
        runApplication();
    }

    public static void loadArguments(String[] args) {
        for (int i = 0; i < args.length; i++ ) {
            switch (args[i]) {
                case "-mode":
                    mode = args[++i];
                    break;
                case "-key":
                    key = args[++i];
                    break;
                case "-data":
                    data = args[++i];
                    break;
                case "-out":
                    out = args[++i];
                    break;
                case "-in":
                    in = args[++i];
                    break;
                case "-alg":
                    alg = args[++i];
                    break;
            }
        }
        if (!Objects.equals(in, "") && !Objects.equals(data, "")) {
            in = "";
        } else if (!Objects.equals(in, "")) {
            try {
                data = new String(Files.readAllBytes(Paths.get(in)));
            } catch (Exception e) {
                System.out.println("File content loading error.");
            }
        }
    }

    public static void runApplication() {
        String message;
        ArrayList<Character> characterArrayList = preprocessMessage(data);
        switch (mode) {
            case "enc":
                message = encryptMessage(characterArrayList);
                break;
            case "dec":
                message = decryptMessage(characterArrayList);
                break;
            default:
                return;
        }
        printMessage(message);
    }

    public static ArrayList<Character> preprocessMessage(String data) {
        char[] messageArray = data.toCharArray();
        Stream<Character> myCharStream = IntStream.range(0, messageArray.length).mapToObj( i -> messageArray[i]);
        return myCharStream.collect(Collectors.toCollection(ArrayList::new));
    }

    public static String encryptMessage(ArrayList<Character> characterArrayList) {
        StringBuilder encryptedMessage = new StringBuilder();
        characterArrayList.forEach( (character -> {
            char newChar;
            if (Objects.equals(alg, "shift")) {
                int alphabet = checkAlphabet(character);
                switch (alphabet) {
                    case Constants.LOWERCASE:
                        newChar = encryptLowercaseCharacterWithShiftAlgorithm(character);
                        break;
                    case Constants.UPPERCASE:
                        newChar = encryptUppercaseCharacterWithShiftAlgorithm(character);
                        break;
                    default:
                        newChar = character;
                }
            } else {
                newChar = encryptCharacterWithUnicodeAlgorithm(character);
            }
            encryptedMessage.append(newChar);
        }));
        return encryptedMessage.toString();
    }

    public static char encryptLowercaseCharacterWithShiftAlgorithm(char character) {
        return (char) (Constants.INT_a + ((int) character + Integer.parseInt(key) - Constants.INT_a) % Constants.ALPHABET_LENGTH);
    }

    public static char encryptUppercaseCharacterWithShiftAlgorithm(char character) {
        return (char) (Constants.INT_A + ((int) character + Integer.parseInt(key) - Constants.INT_A) % Constants.ALPHABET_LENGTH);
    }

    public static char encryptCharacterWithUnicodeAlgorithm(char character) {
        return (char) (Constants.INT_a + ((int) character + Integer.parseInt(key) - Constants.INT_a));
    }

    public static String decryptMessage(ArrayList<Character> characterArrayList) {
        StringBuilder encryptedMessage = new StringBuilder();
        characterArrayList.forEach( (character -> {
            char newChar;
            if (Objects.equals(alg, "shift")) {
                int alphabet = checkAlphabet(character);
                switch (alphabet) {
                    case 1:
                        newChar = decryptLowercaseCharacterWithShiftAlgorithm(character);
                        break;
                    case 2:
                        newChar = decryptUppercaseCharacterWithShiftAlgorithm(character);
                        break;
                    default:
                        newChar = character;
                }

            } else {
                newChar = decryptCharacterWithUnicodeAlgorithm(character);
            }
            encryptedMessage.append(newChar);
        }));
        return encryptedMessage.toString();
    }

    public static char decryptLowercaseCharacterWithShiftAlgorithm(char character) {
        if (((int) character - Integer.parseInt(key) - Constants.INT_a) % Constants.ALPHABET_LENGTH >= 0) {
            return (char) (Constants.INT_a + ((int) character - Integer.parseInt(key) - Constants.INT_a) % Constants.ALPHABET_LENGTH);
        } else {
           return (char) (Constants.INT_a + ((int) character - Integer.parseInt(key) - Constants.INT_a) % Constants.ALPHABET_LENGTH + Constants.ALPHABET_LENGTH);
        }
    }

    public static char decryptUppercaseCharacterWithShiftAlgorithm(char character) {
        if (((int) character - Integer.parseInt(key) - Constants.INT_A) % Constants.ALPHABET_LENGTH >= 0) {
            return (char) (Constants.INT_A + ((int) character - Integer.parseInt(key) - Constants.INT_A) % Constants.ALPHABET_LENGTH);
        } else {
            return (char) (Constants.INT_A + ((int) character - Integer.parseInt(key) - Constants.INT_A) % Constants.ALPHABET_LENGTH + Constants.ALPHABET_LENGTH);
        }
    }

    public static char decryptCharacterWithUnicodeAlgorithm(char character) {
        return (char) ((int) character - Integer.parseInt(key));
    }

    public static void printMessage(String message) {
        if (Objects.equals(out, "")) {
            System.out.println(message);
        } else {
            try (FileWriter writer = new FileWriter(out)) {
                writer.write(message);
            } catch (Exception e) {
                System.out.println("File content writing error.");
            }
        }
    }
    public static int checkAlphabet(char a) {
        if ((int) a >= Constants.INT_a && (int) a <= Constants.INT_z) {
            return 1;
        }
        if ((int) a >= Constants.INT_A && (int) a <= Constants.INT_Z) {
            return 2;
        }
        return 0;
    }
}
