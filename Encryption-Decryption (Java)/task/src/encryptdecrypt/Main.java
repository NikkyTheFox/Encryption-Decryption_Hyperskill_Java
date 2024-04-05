package encryptdecrypt;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    static final int ALPHABET_LENGTH = 26;
    static final int INT_a = 97;
    static final int INT_z = 122;
    static int INT_A = 65;
    static final int INT_Z = 90;

    static String mode = "end";
    static String key = "0";
    static String data = "";
    static String in = "";
    static String out = "";
    static String alg = "shift";

    public static void main(String[] args) {
        loadArguments(args);
        process();
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

    public static void process() {
        String message2;
        ArrayList<Character> characterArrayList = preprocess(data);
        switch (mode) {
            case "enc":
                message2 = encrypt(characterArrayList);
                break;
            case "dec":
                message2 = decrypt(characterArrayList);
                break;
            default:
                return;
        }
        print(message2);
    }

    public static ArrayList<Character> preprocess(String data) {
        char[] messageArray = data.toCharArray();
        Stream<Character> myCharStream = IntStream.range(0, messageArray.length).mapToObj( i -> messageArray[i]);
        return myCharStream.collect(Collectors.toCollection(ArrayList::new));
    }

    public static String encrypt(ArrayList<Character> characterArrayList) {
        StringBuilder encryptedMessage = new StringBuilder();
        characterArrayList.forEach( (character -> {
            char newChar;
            if (Objects.equals(alg, "shift")) {
                int alphabet = checkAlphabet(character);
                switch (alphabet) {
                    case 1:
                        // lowercase
                        newChar = (char) (INT_a + ((int) character + Integer.parseInt(key) - INT_a) % ALPHABET_LENGTH);
                        break;
                    case 2: 
                        // uppercase
                        newChar = (char) (INT_A + ((int) character + Integer.parseInt(key) - INT_A) % ALPHABET_LENGTH);
                        break;
                    default:
                        newChar = character;
                }
            } else {
                newChar = (char) (INT_a + ((int) character + Integer.parseInt(key) - INT_a));
            }
            encryptedMessage.append(newChar);
        }));
        return encryptedMessage.toString();
    }

    public static String decrypt(ArrayList<Character> characterArrayList) {
        StringBuilder encryptedMessage = new StringBuilder();
        characterArrayList.forEach( (character -> {
            char newChar;
            if (Objects.equals(alg, "shift")) {
                int alphabet = checkAlphabet(character);
                switch (alphabet) {
                    case 1:
                        // lowercase
                        if (((int) character - Integer.parseInt(key) - INT_a) % ALPHABET_LENGTH >= 0) {
                            newChar = (char) (INT_a + ((int) character - Integer.parseInt(key) - INT_a) % ALPHABET_LENGTH);
                        } else {
                            newChar = (char) (INT_a + ((int) character - Integer.parseInt(key) - INT_a) % ALPHABET_LENGTH + ALPHABET_LENGTH);
                        }
                        break;
                    case 2:
                        // uppercase
                        if (((int) character - Integer.parseInt(key) - INT_A) % ALPHABET_LENGTH >= 0) {
                            newChar = (char) (INT_A + ((int) character - Integer.parseInt(key) - INT_A) % ALPHABET_LENGTH);
                        } else {
                            newChar = (char) (INT_A + ((int) character - Integer.parseInt(key) - INT_A) % ALPHABET_LENGTH + ALPHABET_LENGTH);
                        }
                        break;
                    default:
                        newChar = character;
                }

            } else {
                newChar = (char) ((int) character - Integer.parseInt(key));
            }
            encryptedMessage.append(newChar);
        }));
        return encryptedMessage.toString();
    }

    public static void print(String message) {
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
        if ((int) a >= INT_a && (int) a <= INT_z) {
            return 1;
        }
        if ((int) a >= INT_A && (int) a <= INT_Z) {
            return 2;
        }
        return 0;
    }
}
