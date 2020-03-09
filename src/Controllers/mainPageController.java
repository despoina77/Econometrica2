/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handlers.DataBase;
import handlers.QuandleApi;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import model.Country;
import model.CountryData;
import model.CountryDataset;
import model.Dataset;


//Η κλάση αυτή περιέχει μεθόδους που θα χρησιμοποιηθούν σε όλο το πρόγραμμα 
//και κυρίως απο τα μενού του Gui
public class mainPageController {
    public static DefaultTableModel gdpModel, oilModel;
   
     /**
     * //Ελέγχει αν υπαρχουν δεδομένα για το ΑΕΠ στη Βάση για να τα χρησιμοποιήσει στην εφαρμογή
     * αλλιώς τα εισάγει από το API.
     * @param country //Παράμετρος τύπου Country για να ελεγχεί αν η συγκεκριμένη χωρα εχει δεδομένα ή όχι
     * @return //Επιστρέφει τα δεδομένα
     */
    public static CountryDataset getGdpData(Country country){
       CountryDataset countryDataset = null;
        
       if(DataBase.alreadyExist(country)){
           countryDataset = getGdpDataFromDb(country); 
       }else{
           countryDataset = getGdpDataFromApi(country);
       }
             
       return countryDataset;
    }
     /**
     * //Ελέγχει αν υπαρχουν δεδομένα για το OIL στη Βάση για να τα χρησιμοποιήσει στην εφαρμογή
     * αλλιώς τα εισάγει από το API.
     * @param country //Παράμετρος τύπου Country για να ελεγχεί αν η συγκεκριμένη χωρα εχει δεδομένα ή όχι
     * @return //Επιστρέφει τα δεδομένα
     */
    public static CountryDataset getOilData(Country country){
       CountryDataset countryDataset = null;
       if(DataBase.alreadyExist(country)){
           countryDataset = getOilDataFromDb(country); 
       }else{
           countryDataset = getOilDataFromApi(country);
       }
       return countryDataset;
    }
    /**
     * //Παίρνει τα δεδομένα του GDP από τη DB και τα εμφανίζει στην εφαρμογή στο σντίστοιχο table.
     * @param countryCode //Παράμετρος τύπου CountryCode για να ελεγχεί τη συγκεκριμένη χώρα σύμφωνα με το κωδικό της
     * @return //Επστρέφει τα στοιχεία (Πεδία "Year,value")που θα εμφανίζονται στην εφαρμογή
     */
    public static CountryDataset getGdpDataFromDb(Country countryCode){
        CountryDataset countrydataset = DataBase.getCountryGdpData(countryCode);        
        gdpModel = new DefaultTableModel();            
        String header[] = new String[] { "Year", "value" };
        gdpModel.setColumnIdentifiers(header);
        try{
            for(CountryData cd : countrydataset.getCountryDataCollection()){ 

                gdpModel.addRow(new Object[]{cd.getDataYear(), cd.getValue()});
            }        
        }catch(Exception ex){
            countrydataset = null;
        }
        return countrydataset;
    }
    /**
     * //Παίρνει τα δεδομένα του OIL από τη DB και τα εμφανίζει στην εφαρμογή στο σντίστοιχο table.
     * @param countryCode ////Παράμετρος τύπου CountryCode για να ελεγχεί τη συγκεκριμένη χώρα σύμφωνα με το κωδικό της
     * @return //Επστρέφει τα στοιχεία (Πεδία "Year,value")που θα εμφανίζονται στην εφαρμογή
     */
    public static CountryDataset getOilDataFromDb(Country countryCode){
        CountryDataset countrydataset = DataBase.getCountryOilData(countryCode);        
        oilModel = new DefaultTableModel();            
        String header[] = new String[] { "Year", "value" };
        oilModel.setColumnIdentifiers(header);
        try{
            for(CountryData cd : countrydataset.getCountryDataCollection()){ 

                oilModel.addRow(new Object[]{cd.getDataYear(), cd.getValue()});
            }      
        }catch(Exception ex){
            countrydataset = null;
        }
      
        return countrydataset;
    }
    /**
     * //Παίρνει τα δεδομένα του GDP από το API τα οποία είναι σε μορφή JSON και κρατάει αυτά που μας χρειάζονται,
     * τα οποία αποθηκεύει σε ενα array List.
     * @param countryCode //Παράμετρος τύπου CountryCode για να ελεγχεί τη συγκεκριμένη χώρα σύμφωνα με το κωδικό της
     * @return //Επστρέφει τα δεδομένα που έχουν αποθηκευθεί στο ArrayList.
     */
    public static CountryDataset getGdpDataFromApi(Country countryCode){
       
        QuandleApi qa = new QuandleApi();        
        CountryDataset cds=null;
        
        String dataset = qa.getGdp(countryCode.getIsoCode());
        //System.out.println(dataset);
        if(dataset!=null){
            GsonBuilder builder = new GsonBuilder(); 
            builder.setPrettyPrinting(); 

            Gson gson = builder.create(); 

            Dataset ds = gson.fromJson(dataset, Dataset.class);    
            cds = ds.getDataset();


            Collection<CountryData> countryDataCollection = new ArrayList<CountryData>();
            gdpModel = new DefaultTableModel();            
            String header[] = new String[] { "Year", "value" };
            gdpModel.setColumnIdentifiers(header);

            for(ArrayList<String> cd : cds.getData()){ 
                CountryData countrydata = new CountryData();
                countrydata.setDataYear(cd.get(0).substring(0,4));
                countrydata.setValue(cd.get(1));
                countrydata.setDataset(cds);
                countryDataCollection.add(countrydata);


                gdpModel.addRow(new Object[]{cd.get(0).substring(0,4), cd.get(1)});

            }
            cds.setStartYear(cds.getStartYear());
            cds.setEndYear(cds.getEndYear());
            //cds.setCountryCode(countryCode);

            cds.setCountryDataCollection(countryDataCollection);
        }
        
        return cds;
    }
    
    /**
     * //Παίρνει τα δεδομένα του OIL από το API τα οποία είναι σε μορφή JSON και κρατάει αυτά που μας χρειάζονται,
     * τα οποία αποθηκεύει σε ενα array List.
     * @param countryCode //Παράμετρος τύπου CountryCode για να ελεγχεί τη συγκεκριμένη χώρα σύμφωνα με το κωδικό της
     * @return //Επστρέφει τα δεδομένα που έχουν αποθηκευθεί στο ArrayList.
     */
    public static CountryDataset getOilDataFromApi(Country countryCode){
        QuandleApi qa = new QuandleApi();        
        CountryDataset cds=null;
        
        String dataset = qa.getOil(countryCode.getIsoCode());
        if(dataset != null){
            GsonBuilder builder = new GsonBuilder(); 
            builder.setPrettyPrinting(); 

            Gson gson = builder.create(); 

            Dataset ds = gson.fromJson(dataset, Dataset.class);    
            cds = ds.getDataset();


            Collection<CountryData> countryDataCollection = new ArrayList<CountryData>();
            oilModel = new DefaultTableModel();            
            String header[] = new String[] { "Year", "value" };
            oilModel.setColumnIdentifiers(header);

            for(ArrayList<String> cd : cds.getData()){ 
                CountryData countrydata = new CountryData();
                countrydata.setDataYear(cd.get(0).substring(0,4));
                countrydata.setValue(cd.get(1));
                countrydata.setDataset(cds);
                countryDataCollection.add(countrydata);


                oilModel.addRow(new Object[]{cd.get(0).substring(0,4), cd.get(1)});

            }
            cds.setStartYear(cds.getStartYear());
            cds.setEndYear(cds.getEndYear());            
            cds.setCountryDataCollection(countryDataCollection);
        }
        return cds;
    }
}
