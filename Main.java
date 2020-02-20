/*
Below is an integration flow on how to use Cashfree's cashgram feature.
Please go through the payout docs here: https://dev.cashfree.com/payouts

The following script contains the following functionalities :
    1.getToken() -> to get auth token to be used in all following calls.
    2.createCashgram() -> to create cashgram.
    3.cashgramGetStatus() -> to get the cashgrams status


All the data used by the script can be found in the config.ini file. This includes the clientId, clientSecret, cashgram section.
You can change keep changing the values in the config file and running the script.
Please enter your clientId and clientSecret, along with the appropriate environment and bank details
*/

import java.net.HttpURLConnection;
import java.net.URL;


import java.io.OutputStream; 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;


import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
 

class Executor{
    private JSONObject config;
    private boolean initialized = false;
    private String env, baseurl, clientId, clientSecret;
    private HashMap<String, String> headers;
    private HashMap<String, String> urls;

    Executor(){
        try{
            FileReader reader = new FileReader("config.json");
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);
            this.config = (JSONObject) obj;
            this.initializeValues();
            this.initialized = true;
        }
        catch(Exception err){
            this.initialized = false;
            System.out.println("err caught in constructor");
            err.printStackTrace();  
        }
    }

    @SuppressWarnings("unchecked")
    private void initializeValues() throws Exception {
        try{
            this.clientId = (String)this.config.get("clientId");
            this.clientSecret = (String)this.config.get("clientSecret");
            this.env = (String)this.config.get("env");

            HashMap<String, String> baseUrls =  (HashMap<String, String>)this.config.get("baseUrl");
            this.baseurl = baseUrls.get(this.env);
            this.urls = (HashMap<String, String>)this.config.get("url");
        }
        catch(Exception err){
            System.out.println("err caught in initialising values");
            throw err;
        }
    }
    
    private HashMap<String, String> createHeaders(String token){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Client-Id", this.clientId);
        headers.put("X-Client-Secret", this.clientSecret);

        if(token != null){
            headers.put("Authorization", "Bearer " + token);
            headers.put("Content-Type", "application/json"); 
        }
        return headers;
    }

    private String callHelper(String method, String finalUrl, HashMap<String,String> headers ,JSONObject data) throws Exception {
        try{
            URL url = new URL(finalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 

            conn.setRequestMethod(method);
            conn.setDoInput(true);

            for(Map.Entry<String, String> entry: headers.entrySet()){
                conn.setRequestProperty(entry.getKey(), entry.getValue().toString());
            }

            if(data != null){
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(data.toString().getBytes());
                os.flush();
            }

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception("Error while making request  : HTTP error code : " + conn.getResponseCode());
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String response,output;
            response = "";
            while ((output = br.readLine()) != null) {
                response += output;
            }
            conn.disconnect();
            return response;

        }
        catch(Exception err){
            System.out.println("Error in posting data");
            throw err;
        }
    }

    public boolean isInitialized(){
        return this.initialized;
    }

    //get auth token
    public String getToken() throws Exception {
        try{
            String finalUrl = this.baseurl + this.urls.get("auth");
            String response = this.callHelper("POST",finalUrl, this.createHeaders(null), null);
            
            JSONParser jsonParser = new JSONParser();
            JSONObject resp = (JSONObject) jsonParser.parse(response);
            
            String status, subCode;
            status = (String)resp.get("status");
            subCode = (String)resp.get("subCode");
            
            if(!(status.equals("SUCCESS")) || !(subCode.equals("200"))){
                throw new Exception("response err: response is incorrect \n" + resp.get("message"));
            }

            JSONObject data = (JSONObject) resp.get("data");
            return (String)data.get("token");
            
        }
        catch(Exception err){
            System.out.println("err caught in getting token");
            throw err;
        }
    }

    //create cashgram
    public void createCashgram(String token) throws Exception {
        try{
            String finalUrl = this.baseurl + this.urls.get("createCashgram");
            HashMap<String, String> headers = this.createHeaders(token);
            JSONObject cashgramDetails = (JSONObject) this.config.get("cashgramDetails");

            String response = this.callHelper("POST",finalUrl, headers, cashgramDetails);

            JSONParser jsonParser = new JSONParser();
            JSONObject resp = (JSONObject) jsonParser.parse(response);
            
            String status, subCode;
            status = (String)resp.get("status");
            subCode = (String)resp.get("subCode");
            
            if(!(status.equals("SUCCESS")) || !(subCode.equals("200"))){
                throw new Exception("response err: response is incorrect \n" + resp.get("message"));
            }

            System.out.println("cashgram created");
        }
        catch(Exception err){
            System.out.println("err caught in creating cashgram");
            throw err;
        }
    }

    //get cashgram status
    public void cashgramGetStatus(String token) throws Exception {
        try{
            JSONObject cashgramDetails = (JSONObject) this.config.get("cashgramDetails");
            String queryString = "?cashgramId="+cashgramDetails.get("cashgramId");
            String finalUrl = this.baseurl + this.urls.get("getCashgramStatus") + queryString;

            HashMap<String, String> headers = this.createHeaders(token);

            String response = this.callHelper("GET", finalUrl, headers, null);

            JSONParser jsonParser = new JSONParser();
            JSONObject resp = (JSONObject) jsonParser.parse(response);
            
            String status, subCode;
            status = (String)resp.get("status");
            subCode = (String)resp.get("subCode");
            
            if(!(status.equals("SUCCESS")) || !(subCode.equals("200"))){
                throw new Exception("response err: response is incorrect \n" + resp.get("message"));
            }

            System.out.println(response);


        }
        catch(Exception err){
            System.out.println("err caught in getting cashgram details");
            throw err;
        }
    }

}   


/*
The flow executed below is:
1. fetching the auth token
2. creating a cashgram
3. getting the status of the cashgram
*/

public class Main {
    public static void main(String[] args){
        try{
            Executor exec = new Executor();
            if(!exec.isInitialized())throw new Exception("executor class not initialised successfully");
            
            String token = exec.getToken();
            exec.createCashgram(token);
            exec.cashgramGetStatus(token);
        }
        catch(Exception e){
            System.out.println("err caught in the main loop");
            e.printStackTrace();
        }
    }
}
