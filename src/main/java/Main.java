import java.util.Scanner;

/**
 * Created by joakimnilfjord on 7/4/2017 AD.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        ManualAnalyzer manualAnalyze = new ManualAnalyzer();
        //System.out.println(manualAnalyze.analyze("fetch Peter from noobs in l loops"));

        while (true) {
            manualAnalyze.resetResults();
            System.out.println("Type in 's' to start the voice recording");
            Scanner sc = new Scanner(System.in);
            String command = sc.next();
            if (command.equals("s")){
                JavaSoundRecorder.startRecording();
                String speech = AudioFileToText.convert();
                System.out.println(manualAnalyze.analyze(speech));

            }
            else {
                System.out.println("Invalid Command");
            }
        }
    }
}
