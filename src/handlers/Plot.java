/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;
import java.awt.Color;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JFrame;
import model.CountryData;
import model.CountryDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

//Με την επιλογή του πλήκτρου Plot το σύστημα εμφανίζει σε αντιπαραβολή (στο ίδιο διάγραμμα 
//δηλαδή για να είναι δυνατή η σύγκριση) σε ξεχωριστό παράθυρο τις 2 χρονοσειρές.
public class Plot extends JFrame{
   
    
    private ArrayList<CountryDataset> countryDatasetList;
    private CountryDataset oil;
    private CountryDataset gdp;
     
    public Plot(final String title, CountryDataset oil, CountryDataset gdp) {      
        //Δημιουργία Τιτλου διαγράμματος
          super(title);
          this.oil = oil;
          this.gdp = gdp;
          this.countryDatasetList = countryDatasetList;
          final String chartTitle = title;
          final XYDataset dataset = createDataset1(); 
          //Δημιουργία απεικονισης των Labels του διαγράμματος
          final JFreeChart chart = ChartFactory.createTimeSeriesChart(
              chartTitle, 
              "Date", 
              "Price Per Unit",
              dataset, 
              true, 
              true, 
              false
          );

          final XYPlot plot = chart.getXYPlot();
          //Δημιουργία δευτερου αξονα για τo OIL data (Oil Consumption) 
          final NumberAxis axis2 = new NumberAxis("Secondary");
          axis2.setAutoRangeIncludesZero(false);
          plot.setRangeAxis(1, axis2);
          
          plot.setDataset(1, createDataset2());
          plot.mapDatasetToRangeAxis(1, 1);
          final XYItemRenderer renderer = plot.getRenderer();
          renderer.setToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
          if (renderer instanceof StandardXYItemRenderer) {
              final StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
              rr.setShapesFilled(true);
          }
          //Μετατροπή του GDP σε εκατομμύρια EURO
          NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
          NumberFormat formatter = NumberFormat.getIntegerInstance();
          rangeAxis.setNumberFormatOverride(formatter);
        //Η κλάση StandardXYItemRenderer μπορεί να σχεδιάσει (α)σχήματα σε κάθε σημείο, ή 
        //(β) γραμμές μεταξύ σημείων, ή (γ) και τα δύο σχήματα και γραμμές.
        //Δεδομενου οτι εχουμε δυο αξονες μια για το time και η αλλη για το GDP
        //τοτε θα κατασκευαστουν 2 κλασεις. 
          final StandardXYItemRenderer renderer0 = new StandardXYItemRenderer();
          renderer0.setSeriesPaint(0, Color.blue);
          renderer0.setPlotLines(true);
          renderer0.setBaseShapesVisible(true);
          renderer0.setToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
          plot.setRenderer(0, renderer0);
          
          final StandardXYItemRenderer renderer1 = new StandardXYItemRenderer();
          renderer1.setSeriesPaint(0, Color.red);
          renderer1.setPlotLines(true);
          renderer1.setBaseShapesVisible(true);
          renderer1.setToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
          plot.setRenderer(1, renderer1);

          //Η κλάση DateAxis εμφανίζει τις ημερομηνίες στους αξονες
          final DateAxis axis = (DateAxis) plot.getDomainAxis();
          axis.setDateFormatOverride(new SimpleDateFormat("yyyy"));
          //Ορίζω μεγεθους του frame
          final ChartPanel chartPanel = new ChartPanel(chart);
          chartPanel.setPreferredSize(new java.awt.Dimension(800, 400));
          setContentPane(chartPanel);

      }
    /**
     * //Εισαγωγή των δεδομένων του GDP στο γράφημα
     * @return //Επιστρέφει τις τιμές που θα εισαχθουν στο γράφημα
     */
      private XYDataset createDataset1() {

          final TimeSeries s1 = new TimeSeries("Oil Data ", Year.class);

          if(this.oil != null){
            for(CountryData oildata: this.oil.getCountryDataCollection()){
               CountryData cd = new CountryData();
               s1.add(new Year(Integer.parseInt(oildata.getDataYear())), Double.parseDouble(oildata.getValue()));
            }
          }
          final TimeSeriesCollection dataset = new TimeSeriesCollection();
          dataset.addSeries(s1);


          return dataset;

      }
      /**
       * //Εισαγωγή των δεδομένων του OIL στο γράφημα
       * @return //Επιστρέφει τα δεδομένα για να εισαχθούν στο γράφημα
       */
      private XYDataset createDataset2() {
          final TimeSeries timeseries = new TimeSeries("Gdp Data ", Year.class); 
          if(this.gdp != null){
            for(CountryData gdpdata: this.gdp.getCountryDataCollection()){
               CountryData cd = new CountryData();                      
               timeseries.add(new Year(Integer.parseInt(gdpdata.getDataYear())), Double.parseDouble(gdpdata.getValue()));
            }
          }
          final TimeSeriesCollection dataset = new TimeSeriesCollection();
          dataset.addSeries(timeseries);

          return dataset;

      }


  }

