import jdk.nashorn.internal.runtime.regexp.joni.Regex;

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
    NumberAnalyzer() {
       createKeyWordsLesser();
       createKeyWordsLarger();
       createEqual();
       createAll();
    }
    public void createAll() {
        keyWordAll.add("anyone");
        keyWordAll.add("all");
        keyWordAll.add("everyone");
        keyWordAll.add("everything");

    }
     public void createEqual(){
        keywordEqual.add("same");
        keywordEqual.add("equally");
        keywordEqual.add("equal");
        keywordEqual.add("equals");
        keywordEqual.add("exact");
        keywordEqual.add("exactly");
    }

    public void createKeyWordsLesser() {
        keywordLesser.add("less");
        keywordLesser.add("below");
        keywordLesser.add("smaller");
        keywordLesser.add("younger");
        keywordLesser.add("lighter");
        keywordLarger.add("lower");
    }
    public void createKeyWordsLarger() {
        keywordLarger.add("above");
        keywordLarger.add("more");
        keywordLarger.add("larger");
        keywordLarger.add("greater");
        keywordLarger.add("older");
        keywordLarger.add("heavier");
        keywordLarger.add("taller");
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
        return result;
    }
    public void findObj(String speech,ArrayList<String> verbsArrayList,HashMap<String,String> result,boolean firstTime ) {
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
    public void findNumbers(String speech,HashSet<String> verbs,HashMap<String,String> result,HashSet<String> keyword,String obj,String l) {
        String toPutIn = "";
        for (String s: keyword) {
           String[] lst = speech.split(s);
           if (lst.length == 2) {
               toPutIn+=l;
               String[] lst2 = lst[1].split(" ");
               for (int i = 0;i < lst2.length;i++) {
                   String word = lst2[i];
                   if (keywordEqual.contains(word)) {
                       if (l.equals(">")) {
                           toPutIn+="=";
                       }
                       else if (l.equals("<")){
                           toPutIn = toPutIn+"=";
                       }
                   }
                   else if (RegEx.containsNumber(lst2[i])) {
                       toPutIn += " "+word;
                       result.put(obj,toPutIn);
                       break;
                   }
               }
           }
       }
    }
    public void updateTableOrColumn(String speech, HashMap<String,String>result) {
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
