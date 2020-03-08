package Main;

import view.GUI;

/**
 * Τμήμα ΑΘΗ-1
 * @author ΑΛΕΞΕΛΗΣ ΠΑΥΛΟΣ - 127928
 * @author ΑΛΕΞΙΑΔΟΥ ΔΕΣΠΟΙΝΑ - 118843
 * @author ΑΝΑΓΝΩΣΤΗΣ ΙΩΑΝΝΗΣ - 118847
 */

public class Main {
       
     //Μέθοδος που θέτει το Look And Feel των παραθύρων σε αυτό των Windows
    public static void setWindowsLookAndFeel() {         
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : 
                    javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        
    }
    
     public static void main(String[] args) {
        
        //Ενεργοποίηση look&feel
        setWindowsLookAndFeel();
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //εμφάνιση του κεντρικού μενου
                GUI mainWindow = new GUI();
                mainWindow.setVisible(true);
               
            }
        });
        
    }
 
}
