package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by joakimnilfjord on 7/12/2017 AD.
 */
public class RegEx {
    public static boolean containsNumber(String speech) {
        String pattern = "\\d";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(speech);
        return m.find();
    }
    public static boolean containsEmail(String speech) {
       return speech.toLowerCase().contains("@".toLowerCase());
    }
}
