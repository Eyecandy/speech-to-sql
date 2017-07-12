import analyzers.GoogleAnalyze;
import analyzers.ManualAnalyzer;
import analyzers.NumberAnalyzer;
import audio.utils.JavaSoundRecorder;
import audio.utils.AudioFileToText;
import utils.RegEx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
/**
 * Created by joakimnilfjord on 7/4/2017 AD.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        ManualAnalyzer manualAnalyze = new ManualAnalyzer();
        GoogleAnalyze googleAnalyze = new GoogleAnalyze();
        NumberAnalyzer numberAnalyzer = new NumberAnalyzer();
        while (true) {
            manualAnalyze.resetResults();
            System.out.println("Type in 's' to start the voice recording or 't' to type in input");
            Scanner sc = new Scanner(System.in).useDelimiter("\n");
            String command = sc.next();
            if (command.equals("s")){
                JavaSoundRecorder.startRecording();
                String speech = AudioFileToText.convert();
                getResult(speech,googleAnalyze,manualAnalyze,numberAnalyzer);
            }
            if (command.equals("t")) {
                System.out.println("ask for something by typing...");
                String speech = sc.next();
                System.out.println(getResult(speech, googleAnalyze, manualAnalyze, numberAnalyzer));
            }
            else {
                System.out.println("Invalid Command");
            }
        }
    }
    public static HashMap<String, String> getResult(String speech, GoogleAnalyze googleAnalyze, ManualAnalyzer manualAnalyzer, NumberAnalyzer numberAnalyzer) throws IOException {
        if (RegEx.containsNumber(speech)) {
            System.out.println("number analyzer");
            return numberAnalyzer.analyzer(speech,manualAnalyzer);
        }
        else if (RegEx.containsEmail(speech)) {
            System.out.println("manual analyzer");
            return manualAnalyzer.analyze(speech);
        }
        else {
            System.out.println("google analyzer");
            googleAnalyze.analyzeSyntaxText(speech);
            return googleAnalyze.analyzeSyntaxText(speech);
        }
    }
}
