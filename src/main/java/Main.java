import analyzers.GoogleAnalyze;
import analyzers.ManualAnalyzer;
import analyzers.NumberAnalyzer;
import audio.utils.JavaSoundRecorder;
import audio.utils.AudioFileToText;
import database.DatabaseManager;
import utils.RegEx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 * Created by joakimnilfjord on 7/4/2017 AD.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        ManualAnalyzer manualAnalyze = new ManualAnalyzer();
        GoogleAnalyze googleAnalyze = new GoogleAnalyze();
        NumberAnalyzer numberAnalyzer = new NumberAnalyzer();

        DatabaseManager databaseManager = new DatabaseManager();

        while (true) {
            manualAnalyze.resetResults();
            System.out.println("Type in 's' to start the voice recording or 't' to type in input");
            Scanner sc = new Scanner(System.in).useDelimiter("\n");
            String command = sc.next();
            String speech = "";
            if (command.equals("s")){
                JavaSoundRecorder.startRecording();
                speech = AudioFileToText.convert();
            } else if (command.equals("t")) {
                System.out.println("ask for something by typing...");speech = sc.next();

            } else if (command.equals("q")) {
                databaseManager.getDbConnection().getConnection().close();
                return;
            } else {
                System.out.println("Invalid Command");
            }
            HashMap<String,String> result =getResult(speech, googleAnalyze, manualAnalyze, numberAnalyzer);
            System.out.println(strip(result));

            //results = getResult(speech, googleAnalyze, manualAnalyze, numberAnalyzer);
            databaseManager.search(strip(result));



        }
    }
    public static HashMap<String, String> getResult(String speech, GoogleAnalyze googleAnalyze, ManualAnalyzer manualAnalyzer, NumberAnalyzer numberAnalyzer) throws IOException {
        if (RegEx.containsNumber(speech)) {
            System.out.println("number analyzer");

            HashMap<String,String> result = numberAnalyzer.analyzer(speech,manualAnalyzer);
            String n = googleAnalyze.analyzeSyntaxText2(result.get("show"));
            result.put("obj",n);
            return result;

        }
        else if (RegEx.containsEmail(speech)) {
            System.out.println("manual analyzer");
            HashMap<String,String> result =manualAnalyzer.analyze(speech);
            //String n = googleAnalyze.analyzeSyntaxText2(result.get("show"));
            //result.put("obj",n);
            return result;
        }
        else {
            System.out.println("google analyzer");
            googleAnalyze.analyzeSyntaxText(speech);
            return googleAnalyze.analyzeSyntaxText(speech);
        }
    }
    public static HashMap<String,String> strip(HashMap<String,String> result) {
        for (String k:result.keySet()) {

            String n = result.get(k);
            if (n!=null) {
                n = n.trim();
                result.put(k,n);
            }
        }

        return result;
    }



}
