import java.util.Scanner;

/**
 * Created by joakimnilfjord on 7/4/2017 AD.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        /*
        Analyze analyze = new Analyze();
        List<Token> tokens =  analyze.analyzeSyntaxText("give me bob");
        for (Token t: tokens) {
            System.out.println(t);

        }
        */
        ManualAnalyzer manualAnalyze = new ManualAnalyzer();
        manualAnalyze.analyze("give me bob from users");



        while (true) {
            System.out.println("Type in 's' to start the voice recording");
            Scanner sc = new Scanner(System.in);
            String command = sc.next();
            if (command.equals("s")){
                JavaSoundRecorder.startRecording();
                AudioFileToText.convert();
            }
            else {
                System.out.println("Invalid Command");
            }
        }
    }
}
