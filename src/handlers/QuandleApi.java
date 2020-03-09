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

//Η κλάση αυτή περιέχει μεθόδους που θα χρησιμοποιηθούν για την εισαγωγή των δεδομένων απο το
//site. Θα κάνει την αποκωδικοποίηση του Json και για ενα επιτευχθεί χρησιμοποιήθηκε 
//η βιβλιοθήκη GSON της Google

public class QuandleApi {
    
    private final String code;
    private final String urlOil;
    private final String urlGdp;
    
    public QuandleApi(){
        this.code = "XkpxuvFYG5yftbRJ6ES8";
        this.urlOil = "https://www.quandl.com/api/v3/datasets/BP/";   
        this.urlGdp = "https://www.quandl.com/api/v3/datasets/WWDI/";
    }
    //μεθοδος που εισάγει τα δεδομένα GDP data απο το json
    //επιστέφει μια λίστα με τις τιμες του dataset
    public String getGdp(String countryCode){        
        String returnData = this.call(this.urlGdp + countryCode+"_NY_GDP_MKTP_CN.json?api_key=" + this.code);
        
        return returnData;
    }    
    //μεθοδος που εισάγει τα δεδομένα OIL data απο το json
    //επιστέφει μια λίστα με τις τιμες του dataset
    public String getOil(String countryCode){                    
        String returnData = this.call(this.urlOil+"OIL_CONSUM_"+countryCode+".json?api_key="+this.code);
        
        return returnData;    
    }
    //Δημιουργεία URL για το OIL & GDP DATA
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
