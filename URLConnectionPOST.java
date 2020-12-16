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

public class Dictionary{
    private static String app_id ;
    private static String app_key;
    
	protected Dictionary(){
	}
	
   //Oxford api 이용을 위한 ID, Key 입력
	public void setKey(String id, String key) {
		app_id = id;
		app_key = key;
	}
	
   //Oxford api에서 가져온 사전 검색 정보를 json파싱으로 선별하는 클래스. 단어 뜻, 예문 등 필요한 정보만 골라낼 수 있음.
	public static String search(String word){
		JSONParser parser = new JSONParser();
		String lexCat = new String();
		String def = new String();
	    String[] ret = new String[2]; // ret[0] -> lexcialCategory, ret[1] -> definition
	    try{
	        final String url = getRequest(buildURL(word));
	        final JSONObject jsonObj = (JSONObject)parser.parse(url);
	        
	        JSONArray results = (JSONArray) jsonObj.get("results"); //"results"정보 내에서 탐색.
	        for(int i = 0; i < results.size(); i++) {
		        JSONArray lexicalEntries = (JSONArray) ((JSONObject)results.get(i)).get("lexicalEntries"); //"lexicalEntries"안에 있는 정보 탐색.
		        if(lexicalEntries == null) continue;
	        	for(int j = 0; j < lexicalEntries.size(); j++) {
	        		//lexcialCategory를 출력
	        		JSONObject lexicalCategory = (JSONObject)((JSONObject)lexicalEntries.get(j)).get("lexicalCategory");
			        JSONArray entries = (JSONArray) ((JSONObject)lexicalEntries.get(j)).get("entries");
			        if(entries == null) continue;
	        		if(lexicalCategory != null)
	        			lexCat = lexicalCategory.get("text").toString();
	        		for(int k = 0; k < entries.size(); k++) {
				        JSONArray senses = (JSONArray) ((JSONObject)entries.get(k)).get("senses");
				        if(senses == null) continue;
				        for(int l = 0; l < senses.size(); l++) {
					        JSONArray definitions = (JSONArray) ((JSONObject)senses.get(l)).get("definitions"); //"definitions"에 있는 정보를 가져옴.
					        if(definitions == null) continue;
					        
					        //한 개만 출력
		        			def = definitions.get(0).toString();
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
	        return "no result (or check your API ID & Key";
	    }
	    return lexCat + '\n' + def;
	}
	
   //Oxford api에 전송할 URL제작. 요구하는 형식에 맞추어 문자열로 제작.
	private static String buildURL(final  String word){
		final String language="en-gb";
	    final String word_id=word.toLowerCase();
	    return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id;
	}
	
   //Oxford api에 url요청을 보내는 클래스.
	private static String getRequest(String link){
	    try{
	    	URL url = new URL(link);
          //헤더 정보
		    HttpsURLConnection urlConnection=(HttpsURLConnection) url.openConnection();
		    urlConnection.setRequestProperty("Accept", "application/json");
		    urlConnection.setRequestProperty("app_id", app_id);
		    urlConnection.setRequestProperty("app_key", app_key);
		    //output을 server로부터 가져오기
		    BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		    StringBuilder stringBuilder=new StringBuilder();
		    String line =null;
		    while ((line=reader.readLine()) !=null){
		    	stringBuilder.append(line + "\n");
		    }
	    	return stringBuilder.toString(); //가져온 정보를 반환.
	    }
	    catch (Exception e){
	    	//e.printStackTrace();
	    	return e.toString();
	    }
	}
}
