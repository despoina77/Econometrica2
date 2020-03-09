/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Depoula
 */
public class Dataset {
    private CountryDataset dataset;
    public Dataset(){
        
    }

    public CountryDataset getDataset() {
        return dataset;
    }

    public void setDataset(CountryDataset dataset) {
        this.dataset = dataset;
    }
}