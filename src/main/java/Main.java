import java.util.Scanner;

/**
 * Created by joakimnilfjord on 7/4/2017 AD.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Starting Application...");
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
