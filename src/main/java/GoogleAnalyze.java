import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Token;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GoogleAnalyze {



    public List<Token> analyzeSyntaxText(String text) throws IOException {

        try (LanguageServiceClient languageServiceClient = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder()
                    .setContent(text).setType(Type.PLAIN_TEXT).build();
            AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16).build();
            AnalyzeSyntaxResponse response = languageServiceClient.analyzeSyntax(request);
            System.out.println(getResults(response.getTokensList()));

            return response.getTokensList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String,String> getResults(List<Token> response) {
        HashMap<String, String> results = new HashMap<>();
        results.put("col",null);
        results.put("obj",null);
        results.put("table",null);
        int verbTagVal = 11;
        int nounVal = 6;
        int amodVal = 5;

        boolean firstVerbFound= false;
        boolean colNotFound = true;
        boolean tableFound = false;

        AtomicBoolean goIn = new AtomicBoolean(false);
        AtomicBoolean goIn2 = new AtomicBoolean(false);
        Iterator<Token> iter = response.iterator();
        while (iter.hasNext()) {
            String s = "";
            Token item = iter.next();
            System.out.println(item.getText().getContent());
            System.out.println("MAIN: "+item.getLemma() + " " + item.getPartOfSpeech().getTag()+ " "+item.getPartOfSpeech().getTagValue() + " " +item
           .getDependencyEdge().getLabel() + " "+item.getDependencyEdge().getLabelValue());
            int tagVal =item.getPartOfSpeech().getTagValue();
            if (!firstVerbFound && tagVal == verbTagVal) {
                firstVerbFound = true;
                String obj1 = findNextObject1(iter,goIn,s);
                if (obj1 == null) {
                    return results;
                }

                results.put("obj",obj1);
            }

            else if ((tagVal == 2|| goIn.get()) && colNotFound) {
                if (goIn.get()) {
                    if (tagVal == 6 || item.getDependencyEdge().getLabelValue() == amodVal) {
                        s = item.getText().getContent()+" ";
                    }
                }

                String col =findNextObject1(iter,goIn2,s);
                if (col == null) {
                    return results;
                }
                colNotFound= false;
                results.put("col",col);
            }
            else if ((tagVal== 2|| goIn2.get())  && !tableFound ) {
                if (tagVal == 6 || item.getDependencyEdge().getLabelValue() == amodVal) {
                    s = item.getText().getContent()+" ";
                }
                String table =findNextObject1(iter,goIn,s);

                if (table == null) {
                    return results;
                }

                tableFound = true;
                results.put("table",table);

            }

        }
        return results;
    }

    public static String findNextObject1(Iterator<Token> iter,AtomicBoolean goIn,String s) {
        int amodVal = 5;
        int nounVal = 6;
        int numVal = 7;

        boolean firstNounFound = false;
        while (iter.hasNext()  ) {
            Token item = iter.next();
            System.out.println("HELPER: "+item.getText().getContent() + " " + item.getPartOfSpeech().getTag()+ " "+item.getPartOfSpeech().getTagValue() + " " +item
                    .getDependencyEdge().getLabel() + " "+item.getDependencyEdge().getLabelValue());
            int depEdgeLabel = item.getDependencyEdge().getLabelValue();
            int tagVal = item.getPartOfSpeech().getTagValue();
            if (!firstNounFound) {
                if (tagVal == nounVal || depEdgeLabel == amodVal || tagVal == numVal) {
                    s+=item.getText().getContent()+" ";
                    firstNounFound = true;
                }
            }
            else if (firstNounFound) {
                if (tagVal == nounVal) {
                    s+=item.getText().getContent();
                }
                else {
                    if (item.getPartOfSpeech().getTagValue() == 2) {
                        goIn.set(true);
                    }

                    break;
                }

            }
        }
        if (s.length() ==0) {
            return null;
        }
        else {

            return s;
        }



    }




}