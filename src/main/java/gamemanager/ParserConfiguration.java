package gamemanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Class used to read data from configuration File.
 * On this file all the information are stored: paths and constant of game.
 */
public class ParserConfiguration {
    ParserConfiguration(){}

    /**
     * Generic method used to read a String
     * @param configuration The identifier of the configuration item. It's case-insensitive
     * @return A string containing the configuration item requested. If not found, "NO RESULT FOUND" is returned
     */
    public static String parse(String configuration) {
        Scanner scanner;
        String configurationFile = "src" + File.separator + "resources" + File.separator + "configuration.txt";
        String toSearch = configuration.toLowerCase();

        try {
            scanner = new Scanner(new File(configurationFile));
        } catch (FileNotFoundException e) {
            throw new exception.FileNotFoundExceptionBis("File not found");
        }

        while(scanner.hasNextLine()){
            if(scanner.nextLine().toLowerCase().startsWith("-" + toSearch)){
                String ret = scanner.nextLine();
                scanner.close();
                return ret;
            }
        }
        scanner.close();

        return "NO RESULT FOUND";
    }

    /**
     * Method used to read a path. Automatically build the string
     * @param configuration The identifier of the configuration item. It's case-insensitive
     * @return A string containing the path. If not found, an invalid path is returned
     */
    public static String parsePath(String configuration){
        return String.join(
                File.separator,
                Arrays.asList(parse(configuration).split(" ")
                )
        );
    }

    public static int parseInt(String configuration){
        return Integer.parseInt(ParserConfiguration.parse(configuration));
    }
}
