package URLConnectionPOST;

import  javax.net.ssl.HttpsURLConnection;
import  java.io.BufferedReader;
import  java.io.InputStreamReader;
import  java.net.URL;

public class Oxford{	
	public static void main(String[] args) throws Exception {
		Oxford Conn = new Oxford();
		String word = "cat";
		String URL = Conn.buildURL(word);
		String result = Conn.getRequest(URL);
		System.out.println(result);
	}
	
   private String buildURL(final  String word){
    final String language="en-gb";
    final String word_id=word.toLowerCase();
    return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id;
  }
   
   private String buildURLFrequency(final  String word){
    final String language="en-gb";
    final String word_id=word.toLowerCase();
    return "https://gad-proxy-prod-leap-2-1.us-east-1.elasticbeanstalk.com:443/api/v2/stats/frequency/word/" + language + "/?corpus=nmc&lemma=" + word_id;
  }
   
  private String getRequest(String link){
    final  String app_id="a811d8d9";
    final  String app_key="653128da3a722b9420f1474c6952f29b";
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
    }catch (Exception e){
       e.printStackTrace();
       return e.toString();
    }
  }
}