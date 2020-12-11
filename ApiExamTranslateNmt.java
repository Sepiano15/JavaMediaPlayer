package URLConnectionPOST;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

// 네이버 기계번역 (Papago SMT) API 예제
public class ApiExamTranslateNmt {

    public static void main(String[] args) throws Exception {
        String clientId = "xtx3z1QXWyuj5UGbfgWF";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "G3ZO6HjQK6";//애플리케이션 클라이언트 시크릿값";

        String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
        String Str = "dog"; //번역할 단어. 파파고에서는 문장도 가능.
        String text;
        
        try {
            text = URLEncoder.encode(Str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String responseBody = post(apiURL, requestHeaders, text);
        //System.out.println(responseBody);
        
        //번역 결과만 추출
        String[] array = responseBody.split(":"); //결과값을 배열로 나눔.
        int idx = array[8].indexOf(","); //array[8]은 번역한 결과 .
        String ex_result = array[8].substring(0, idx);
        String result = ex_result.substring(1,ex_result.length()-1); //큰 따옴표 제거
        System.out.println("파파고 번역 결과 : " + result);
        
        //영영사전 예문 불러오기
        HttpConnectionExample.sendGet(Str);
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String text){
        HttpURLConnection con = connect(apiUrl);
        String postParams = "source=en&target=ko&text=" + text; //원본언어: 영어 (en) -> 목적언어: 한국어 (ko)
        
        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
