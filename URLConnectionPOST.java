package globse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import  javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import  java.io.InputStreamReader;
import  java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class URLConnectionPOST extends UnicastRemoteObject{
   public static void main(String[] args) {
       String mean = dictionary("cat");
    }
   
   protected URLConnectionPOST() throws RemoteException {
   }

   public static String dictionary(String word){
      JSONParser parser = new JSONParser();
       String ret="404";
       try{
           final String url = getRequest(buildURL(word));
           final JSONObject jsonObj = (JSONObject)parser.parse(url);
           
           JSONArray results = (JSONArray) jsonObj.get("results");
           for(int i = 0; i < results.size(); i++) {
              JSONArray lexicalEntries = (JSONArray) ((JSONObject)results.get(i)).get("lexicalEntries");
              if(lexicalEntries == null) continue;
              for(int j = 0; j < lexicalEntries.size(); j++) {
                 //lexcialCategory를 출력
                 JSONObject lexicalCategory = (JSONObject)((JSONObject)lexicalEntries.get(j)).get("lexicalCategory");
                 if(lexicalCategory != null) System.out.println(lexicalCategory.get("text").toString());
                 
                 JSONArray entries = (JSONArray) ((JSONObject)lexicalEntries.get(j)).get("entries");
                 if(entries == null) continue;
                 for(int k = 0; k < entries.size(); k++) {
                    JSONArray senses = (JSONArray) ((JSONObject)entries.get(k)).get("senses");
                    if(senses == null) continue;
                    for(int l = 0; l < senses.size(); l++) {
                       JSONArray definitions = (JSONArray) ((JSONObject)senses.get(l)).get("definitions");
                       if(definitions == null) continue;
                       
                       //한 개만 출력
                       System.out.println(definitions.get(0).toString());
                       i = results.size() + 1;
                       j = lexicalEntries.size() + 1;
                       k = entries.size() + 1;
                       l = senses.size() + 1;
                       
                       /*전부 출력
                       for(int m = 0; m < definitions.size(); m++) {
                            System.out.println(definitions.get(m).toString());
                       }
                       */
                    }
                 }
              }
           }
           
       }
       catch (Exception e){
           System.out.println(e);
       }
       return ret;
   }
   private static String buildURL(final  String word){
      final String language="en-gb";
       final String word_id=word.toLowerCase();
       return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id;
   }
   
   private static String getRequest(String link){
       final String app_id="a811d8d9";
       final String app_key= "653128da3a722b9420f1474c6952f29b";
       try{
          URL url = new URL(link);
          HttpsURLConnection urlConnection=(HttpsURLConnection) url.openConnection();
          urlConnection.setRequestProperty("Accept", "application/json");
          urlConnection.setRequestProperty("app_id", app_id);
          urlConnection.setRequestProperty("app_key", app_key);
          // read the output from the server
          BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
          StringBuilder stringBuilder=new StringBuilder();
          String line =null;
          while ((line=reader.readLine()) !=null){
             stringBuilder.append(line + "\n");
          }
          return stringBuilder.toString();
       }
       catch (Exception e){
          e.printStackTrace();
          return e.toString();
       }
   }
}
