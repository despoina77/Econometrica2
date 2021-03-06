/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;
import model.Country;
import model.CountryDataset;

/**
 *
 * @author Depoula
 */
public class DataBase {      
    /**
 * //Δημιουργία entity Manager
 */
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("econometrica2PU");
    private static EntityManager em = emf.createEntityManager();        
    /**
     * //Μέθοδος για να μην αποθηκεύονται χώρες που ήδη υπάρχουν στη Βάση Δεδομένων
     * @param country //Παράμετρος τύπου country για να ελέγχει αν η χώρα είναι αποθηκευμένη.
     * @return //Επιστρέφει true/false σαν αποτέλεσμα του ελεγχου ότι υπάρχει ή όχι η χώρα
     */
    public static boolean alreadyExist(Country country){
        
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Country c where c.isoCode = :isoCode", Long.class);
        long countryCount = query.setParameter("isoCode", country.getIsoCode()).getSingleResult();
        
        if(countryCount>0)
            return true;
        else
            return false;
    }
    /**
     *  //Επιλέγει τα δεδομένα που αφορούν το OIL
     * @param country //Παράμετρος τύπου country για να επιλεγονται τα δεδομένα ανάλογα τη χώρα
     * @return //Επιστρέφει τις τιμές του OIL
     */
    public static CountryDataset getCountryOilData(Country country){
        CountryDataset c = null;
        try{
            TypedQuery<CountryDataset> query = em.createQuery("SELECT c FROM CountryDataset c where c.countryCode = :countryCode and c.name like :name", CountryDataset.class);
            query.setParameter("name", "%Oil %");
            query.setParameter("countryCode", country);
            c = query.getSingleResult();
        }catch(Exception ex){
            System.out.println("no data found");
        }
        
        
        return c;
    }
    /**
     *  //Επιλέγει τα δεδομένα που αφορούν το GDP
     * @param country //Παράμετρος τύπου country για να επιλεγονται τα δεδομένα ανάλογα τη χώρα
     * @return //Επιστρέφει τις τιμές του GDP
     */
    public static CountryDataset getCountryGdpData(Country country){
        CountryDataset c = null;
        try{
            TypedQuery<CountryDataset> query = em.createQuery("SELECT c FROM CountryDataset c where c.countryCode = :countryCode and c.name like :name", CountryDataset.class);
            query.setParameter("name", "%GDP %");
            query.setParameter("countryCode", country);
            c = query.getSingleResult();
        }catch(Exception ex){
            System.out.println("no data found");
        }
        return c;
    }
    /**
     * //Διαγραφή των δεδομένων της βάσης μέσω του entity Manager
     */
    public static void deleteDB() {
       int deleteMessege = JOptionPane.showConfirmDialog(null, "Tα δεδομένα θα διαγραφούν. Είστε Σίγουροι;", "Διγραφή Δεδομένων", JOptionPane.YES_NO_OPTION);
        
        if (deleteMessege == JOptionPane.YES_OPTION) {
            try {
                EntityTransaction et = em.getTransaction();
                et.begin();
                
                Query q;
                q = em.createQuery("DELETE FROM CountryData");
                q.executeUpdate();
                q = em.createQuery("DELETE FROM CountryDataset");
                q.executeUpdate();
                q = em.createQuery("DELETE FROM Country");
                q.executeUpdate();
               

                em.getTransaction().commit();

                JOptionPane.showMessageDialog(null, "Τα δεδομένα διαγράφηκαν ", "Διγραφή Δεδομένων", JOptionPane.INFORMATION_MESSAGE);
    
            } catch (Exception ex) {
                //Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                em.getTransaction().rollback();
            }
        }
    }
    // Αποθήκευση δεδομένων από JSON στη βαση δεδομένων
    public static void save(Country country) {
           //System.out.println("ok");
            em.getTransaction().begin();
            em.persist(country);
            em.flush();
            em.getTransaction().commit();
            em.clear();            
            
    }
    
}
