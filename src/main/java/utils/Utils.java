package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
    private Utils() {}

    public static Properties readProperties() throws IOException {
        Properties prop = new Properties();
        InputStream in = Utils.class.getClassLoader().getResourceAsStream("speechsql.properties");
        //System.out.println(in);
        prop.load(in);
        in.close();
        return prop;
    }
}
