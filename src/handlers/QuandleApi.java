/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 *
 * @author Depoula
 */
public class QuandleApi {
    
    private final String code;
    private final String urlOil;
    private final String urlGdp;
    
    public QuandleApi(){
        this.code = "XkpxuvFYG5yftbRJ6ES8";
        this.urlOil = "https://www.quandl.com/api/v3/datasets/BP/";   
        this.urlGdp = "https://www.quandl.com/api/v3/datasets/WWDI/";
    }
    
    public String getGdp(String countryCode){        
        String returnData = this.call(this.urlGdp + countryCode+"_NY_GDP_MKTP_CN.json?api_key=" + this.code);
        System.out.println(returnData);
        return returnData;
    }    
    
    public String getOil(String countryCode){                    
        String returnData = this.call(this.urlOil+"OIL_CONSUM_"+countryCode+".json?api_key="+this.code);
        System.out.println(returnData);
        return returnData;    
    }
    
    public String call(String url){
        OkHttpClient client = new OkHttpClient();
        System.out.println(url);
        Request request = new Request.Builder().url(url).build();
        String responseString = null;
        
        try(Response response = client.newCall(request).execute()){
            if(response.isSuccessful() && response.body() != null){
                responseString = response.body().string();                                
            }
        }catch(IOException e){
            e.printStackTrace();
            
        }
        
        return responseString;
    }
    
    
    
}
