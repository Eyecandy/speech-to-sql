import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by joakimnilfjord on 7/8/2017 AD.
 */
public class ManualAnalyzer {
    HashSet<String> verbs = new HashSet<>();
    HashSet<String> prepositions = new HashSet<>();
    HashSet<String> keyWords = new HashSet<>();

    public static void main(String[] args) {

    }

    ManualAnalyzer() {
        createNaturalSelects();
        createTablesAndColumnsSearc();
    }

    public void createNaturalSelects() {
        verbs.add("give me ");
        verbs.add("fetch ");
        verbs.add("search for ");
        verbs.add("find ");
    }
    public void createTablesAndColumnsSearc(){
        prepositions.add("from ");
        prepositions.add("in ");

    }
    public void createKeyWords() {

    }
    public void analyze(String naturalText) {
        String[] hello = {};
        for (String keyWord:verbs) {
            hello= naturalText.split(keyWord);
            if (hello.length == 2) {
                break;
            }
        }
        for (String h : hello) {
            System.out.println(h);
        }





    }







}
