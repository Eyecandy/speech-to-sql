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
    HashMap<String, String> results = new HashMap<>();


    ManualAnalyzer() {
        createNaturalSelects();
        createTablesAndColumnsSearc();
        results.put("obj", null);
        results.put("col", null);
        results.put("table", null);
    }

    public void createNaturalSelects() {
        verbs.add("give me ");
        verbs.add("fetch ");
        verbs.add("search for ");
        verbs.add("find ");
        verbs.add("find me");
        verbs.add("get me");
        verbs.add("get ");
        verbs.add("search ");
    }

    public void createTablesAndColumnsSearc() {
        prepositions.add("from ");
        prepositions.add("in ");

    }
    public void resetResults() {
        results.put("obj",null);
        results.put("col",null);
        results.put("table",null);
    }

    public void createKeyWords() {

    }

    public HashMap<String, String> analyze(String naturalText) {
        String splitNaturalText = findVerbAndSeparate(naturalText);
        String doubleSplitNaturalText = findPreposition(splitNaturalText);
        findPrepositionNo2(doubleSplitNaturalText);
        return results;
    }

    public String findVerbAndSeparate(String naturalText) {
        String[] verbAndTheRest = {};
        for (String keyWord : verbs) {
            verbAndTheRest = naturalText.split(keyWord);
            if (verbAndTheRest.length == 2) {
                results.put("obj", verbAndTheRest[1]);
                return verbAndTheRest[1];
            }
        }
        return naturalText;
    }

    public String findPreposition(String naturalText) {
        String[] prepAndTheRest = {};
        for (String prep : prepositions) {
            prepAndTheRest = naturalText.split(prep, 2);
            if (prepAndTheRest.length == 2 && (prepAndTheRest[1].split(" in ").length > 1 ||
            prepAndTheRest[1].split(" from ").length > 2)) {
                results.put("obj", prepAndTheRest[0]);
                results.put("col", prepAndTheRest[1]);
                naturalText = prepAndTheRest[1];
                break;
            } else if (prepAndTheRest.length == 2) {
                results.put("obj", prepAndTheRest[0]);
                results.put("table", prepAndTheRest[1]);
                naturalText = prepAndTheRest[0];
                break;
            }
        }
        return naturalText;
    }

    public void findPrepositionNo2(String naturalText) {

        String colVal = results.get("col");
        String objVal = results.get("obj");
        String tableVal = results.get("table");
        String[] inObj = objVal.split(" in ");
        String[] fromObj = objVal.split(" from ");
        if (tableVal == null && colVal != null) {
            System.out.println("table");
            String[] inCol = colVal.split(" in ");
            String[] fromCol = colVal.split("from");

            if (inCol.length > 1) {
                System.out.println("inCol");
                results.put("col",inCol[0]);
                results.put("table",inCol[1]);
            }
            else if (fromCol.length > 1) {
                System.out.println("fromCol");
                results.put("col",fromCol[0]);
                results.put("table",fromCol[1]);
            }
            else if (inObj.length > 1 || fromObj.length > 1) {
                if (inObj.length > 1) {
                    System.out.println(inObj[0]);
                    System.out.println(inObj[1]);
                    results.put("obj",inObj[0]);
                    results.put("table",inObj[1]);
                }
                else if (fromObj.length > 1) {
                    System.out.println("fromObj");
                    results.put("obj",fromObj[0]);
                    results.put("table",fromObj[1]);
                }
            }
        }
        else if (colVal == null && tableVal != null) {
            System.out.println("col");

            if (inObj.length > 1) {
                System.out.println("inObj");
                results.put("obj",inObj[0]);
                results.put("col",inObj[1]);
            }
            else if (fromObj.length > 1) {
                System.out.println("fromObj");
                results.put("obj",fromObj[0]);
                results.put("col",fromObj[1]);
            }
        }


    }

}
