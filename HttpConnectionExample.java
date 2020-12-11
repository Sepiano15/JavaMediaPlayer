package URLConnectionPOST;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import javax.net.ssl.HttpsURLConnection;

public class HttpConnectionExample {

    public static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception{
    	HttpConnectionExample httpConn = new HttpConnectionExample();

        System.out.println("HttpConnect Get");
        httpConn.sendGet("cat");

    }


    /**
     * Get ���
     * @throws Exception
     */
    static void sendGet(String Str) throws Exception {
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + Str;
        //https://glosbe.com/gapi/translate?from=eng&dest=kor&format=json&pretty=true&phrase=text
        //https://api.dictionaryapi.dev/api/v2/entries/en/cat

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //���۹��
        con.setRequestMethod("GET");
        //Request Header ����
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setConnectTimeout(10000);       //���ؼ�Ÿ�Ӿƿ� 10��
        con.setReadTimeout(5000);           //��������ȸ Ÿ�Ӿƿ� 5��

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        Charset charset = Charset.forName("UTF-8");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),charset));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String responseBody = response.toString();
        String array[] = responseBody.split(":");
        System.out.println(Arrays.toString(array));
        //System.out.println(responseBody);
        //System.out.println(responseBody.indexOf("example"));
        
        //String[] array = responseBody.split(":"); //������� �迭�� ����.
        /*
        int idx = array[8].indexOf(","); //array[8]�� ������ ��� .
        String ex_result = array[8].substring(0, idx);
        String result = ex_result.substring(1,ex_result.length()-1); //ū ����ǥ ����
		*/
        //System.out.println(array[0]);
    }

}
