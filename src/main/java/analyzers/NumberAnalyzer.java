package analyzers;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by joakimnilfjord on 7/12/2017 AD.
 */
public class NumberAnalyzer {
    private HashSet<String> keywordLarger = new HashSet<>();
    private HashSet<String> keywordLesser = new HashSet<>();
    private HashSet<String> keywordEqual = new HashSet<>();
    private HashSet<String> keyWordAll = new HashSet<>();
    public NumberAnalyzer() {
       createKeyWordsLesser();
       createKeyWordsLarger();
       createEqual();
       createAll();
    }
    private void createAll() {
        keyWordAll.add("anyone");
        keyWordAll.add("all");
        keyWordAll.add("everyone");
        keyWordAll.add("everything");

    }
     private void createEqual(){
        keywordEqual.add("same");
        keywordEqual.add("equally");
        keywordEqual.add("equal");
        keywordEqual.add("equals");
        keywordEqual.add("exact");
        keywordEqual.add("exactly");
    }

    private void createKeyWordsLesser() {
        keywordLesser.add("less");
        keywordLesser.add("below");
        keywordLesser.add("smaller");
        keywordLesser.add("older");
        keywordLesser.add("lighter");
        keywordLesser.add("lower");
        keywordLesser.add("lesser");
        keywordLesser.add("shorter");

    }
    public void createKeyWordsLarger() {
        keywordLarger.add("above");
        keywordLarger.add("more");
        keywordLarger.add("larger");
        keywordLarger.add("greater");
        keywordLarger.add("younger");
        keywordLarger.add("heavier");
        keywordLarger.add("taller");
        keywordLarger.add("bigger");
        keywordLarger.add("longer");

    }


    public HashMap<String,String> analyzer(String speech, ManualAnalyzer manualAnalyzer) {
        HashSet<String> verbs = manualAnalyzer.getVerbs();
        ArrayList<String> verbsArrayList = manualAnalyzer.getVerbsArray();
        HashMap<String,String> result  = new HashMap<>();
        updateTableOrColumn(speech,result);
        boolean firstTime = true;
        findObj(speech,verbsArrayList,result,firstTime);
        findNumbers(speech,verbs,result,keywordLarger,"obj1",">");
        findNumbers(speech,verbs,result,keywordLesser,"obj2","<");
        firstTime = false;
        findObj(speech,verbsArrayList,result,firstTime);

        //removeShow(result);

        return result;
    }
    private void removeShow(HashMap<String,String> result) {
         String s = result.get("show");
         if (keywordLarger.contains(s) || keywordLesser.contains(s)) {
             result.put("show","all");
         }
    }
    private void findObj(String speech,ArrayList<String> verbsArrayList,HashMap<String,String> result,boolean firstTime ) {
        boolean resultSetHasBeenUpdated = false;
        for (String s: result.keySet()) {
            if (s.equals("show")) {continue;}
            if (result.get(s) != null) {
                resultSetHasBeenUpdated = true;
                break;
            }
        }
        if (!resultSetHasBeenUpdated||firstTime) {
            for (String s:verbsArrayList) {
                String[] lst = speech.split(s);
                if (lst.length == 2) {
                    String word = lst[1].split(" ")[0];
                    if (keyWordAll.contains(word)) {
                        result.put("show","all");
                    }
                    else {
                        result.put("show",word);
                    }
                    break;
                }
            }
        }
    }
    private void findNumbers(String speech,HashSet<String> verbs,HashMap<String,String> result,HashSet<String> keyword,String obj,String l) {
        String toPutIn = "";
        boolean x = true;
        boolean y = true;
        for (String s: keyword) {
            if (!x) {break;}
           String[] lst = speech.split(s);
           if (lst.length == 2) {
               toPutIn+=l;
               String[] lst2 = lst[1].split(" ");
               for (int i = 0;i < lst2.length;i++) {
                   String word = lst2[i];
                   if (keywordEqual.contains(word) && y) {
                       if (l.equals(">")) {
                           toPutIn+="=";
                       }
                       else if (l.equals("<")){
                           toPutIn = toPutIn+"=";
                       }
                       y = false;
                   }
                   else if (utils.RegEx.containsNumber(lst2[i])) {
                       toPutIn += " "+word;
                       result.put(obj,toPutIn);
                       x = false;
                       break;

                   }
               }
           }
       }
    }
    private void updateTableOrColumn(String speech, HashMap<String,String>result) {
        boolean foundPrep1 = false;
        boolean foundPrep2 = false;
        boolean open = true;
        String[] lst = speech.split(" ");
        for (int i = 0;i < lst.length;i++) {
            String s = lst[i];
            if (foundPrep1 && !foundPrep2 && open) {
                result.put("col",s);
                open = false;
            }
            else if (foundPrep2) {
                result.put("table",s);
                break;
            }
            if (s.equals("from") || s.equals("in") ) {
                System.out.println(s);
                if (!foundPrep1) {
                    foundPrep1 = true;
                }
                else {
                    foundPrep2 = true;
                }
            }
        }
    }
}
