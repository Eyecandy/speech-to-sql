package analyzers;

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

    public String analyzeSyntaxText2(String text) throws IOException {

        try (LanguageServiceClient languageServiceClient = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder()
                    .setContent(text).setType(Type.PLAIN_TEXT).build();
            AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16).build();
            AnalyzeSyntaxResponse response = languageServiceClient.analyzeSyntax(request);

            return response.getTokensList().get(0).getLemma().toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public HashMap<String, String> analyzeSyntaxText(String text) throws IOException {

        try (LanguageServiceClient languageServiceClient = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder()
                    .setContent(text).setType(Type.PLAIN_TEXT).build();
            AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16).build();
            AnalyzeSyntaxResponse response = languageServiceClient.analyzeSyntax(request);
            System.out.println(getResults(response.getTokensList()));
            return getResults(response.getTokensList());
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

        AtomicBoolean goIn1 = new AtomicBoolean(false);
        AtomicBoolean goIn2 = new AtomicBoolean(false);
        Iterator<Token> iter = response.iterator();
        while (iter.hasNext()) {
            String s = "";
            Token item = iter.next();
            int tagVal =item.getPartOfSpeech().getTagValue();
            if (!firstVerbFound && tagVal == verbTagVal) {
                firstVerbFound = true;
                String obj1 = findNextObject1(iter,goIn1,s);
                if (obj1 == null) {
                    return results;
                }

                results.put("obj",obj1);
            }

            else if ((tagVal == 2|| goIn1.get()) && colNotFound && firstVerbFound) {

                if (goIn1.get()) {
                    if (tagVal == 6 || item.getDependencyEdge().getLabelValue() == amodVal) {
                        s = item.getLemma()+" ";
                    }
                }
                String col =findNextObject1(iter,goIn2,s);
                if (col == null) {
                    return results;
                }

                goIn1.set(false);
                colNotFound= false;
                results.put("col",col);
            }
            else if ((tagVal== 2|| goIn2.get())  && !tableFound  && firstVerbFound) {
                if (tagVal == 6 || item.getDependencyEdge().getLabelValue() == amodVal) {
                    s = item.getLemma()+" ";
                }
                String table =findNextObject1(iter,goIn2,s);
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

        boolean firstNounFound = false;
        while (iter.hasNext()  ) {
            Token item = iter.next();
            int depEdgeLabel = item.getDependencyEdge().getLabelValue();
            int tagVal = item.getPartOfSpeech().getTagValue();
            if (!firstNounFound && s.length() ==0) {
                if (tagVal == nounVal || depEdgeLabel == amodVal) {
                    s+=item.getLemma();
                    firstNounFound = true;
                }
            }
            else if (firstNounFound || s.length() != 0) {
                if (tagVal == nounVal) {
                    s+=" "+item.getLemma();
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