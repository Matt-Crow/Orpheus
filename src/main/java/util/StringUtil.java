package util;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author Matt
 */
public class StringUtil {
    public static String entab(String s){
        StringBuilder bui = new StringBuilder();
        /*
        Arrays.stream(s.split("\n")).map((line)->{
            return "\t" + line;
        }).collect(Collectors.joining("\t"));
        */
        return Arrays.stream(s.split("\n")).collect(Collectors.joining("\n\t", "\t", ""));
    }
}
