package com.example.sde.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.sde.data.DriversShipment;
import com.example.sde.data.InformationResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final MutableLiveData<List<DriversShipment>> drivers;
    private final MutableLiveData<Boolean> proposal;
    private final MutableLiveData<Integer> updatePosition;
    private final MutableLiveData<String> msjDialog;
    private final List<String> shipments;
    private DriversShipment proposedShipment;
    private int proposedPosition;

    public MainViewModel(@NonNull Application application) {
        super(application);
        drivers = new MutableLiveData<>();
        proposal = new MutableLiveData<>();
        updatePosition = new MutableLiveData<>();
        msjDialog = new MutableLiveData<>();
        shipments = new ArrayList<>();

        proposedPosition = -1;
    }

    public void getFileData(String fileData){
        try{
            InformationResponse data = new Gson().fromJson(fileData,InformationResponse.class);
            List<DriversShipment> tempDrivers = new ArrayList<>();

            if( data.getDrivers().size() > 0 && data.getShipments().size() > 0){

                shipments.clear();
                shipments.addAll(data.getShipments());

                for(String driver : data.getDrivers()){
                    DriversShipment driversShipment = new DriversShipment(driver);
                    tempDrivers.add(driversShipment);
                }
            }
            proposalDecline();
            drivers.postValue(tempDrivers);
        }catch (Exception e){
            msjDialog.postValue("Error getting data");
            e.printStackTrace();
        }
    }

    private int commonFactors(String driver, String shipment) {
        int factor = 0;
        for( int i = 1; i<Math.min(driver.length(),shipment.length());i++){
            if( driver.length()%i == 0 && shipment.length()%i ==0){
                factor++;
            }
        }
        return factor;
    }

    private double getNumberConsonants(String driver) {
        String consonants = driver.replaceAll("(?i)[[b-z&&[^eiou]]]","");
        return driver.length() - consonants.length();
    }

    private int getNumberVowels(String driver) {
        return driver.replaceAll("(?i)[^aeiou]","").length();
    }

    public MutableLiveData<List<DriversShipment>> getDrivers() {
        return drivers;
    }

    public void proposeShipment(int position) {
        if( drivers.getValue() != null){
            DriversShipment driver = drivers.getValue().get(position);
            if(!driver.isRoute()){
                calculateSS(driver, position);
            }
            else{
                msjDialog.postValue("Driver already has route assigned");
            }
        }
        else{
            msjDialog.postValue("Error retrieving information");
        }


    }

    private void calculateSS(DriversShipment driver, int position){
        double ss = 0.0;
        for(String shipment : shipments){
            if(shipment.length() % 2 == 0){
                ss = getNumberVowels(driver.getDriver())*1.5;
            }
            else{
                ss = getNumberConsonants(driver.getDriver())*1;
            }

            int factors = commonFactors(driver.getDriver(),shipment);
            if( factors > 1){
                ss = ss*1.5;
            }

            if(ss > driver.getSs()){
                driver.setSs(ss);
                driver.setShipment(shipment);
            }
        }
        driver.setRoute(true);
        proposedShipment = driver;
        proposedPosition = position;

        proposal.postValue(true);
    }

    public DriversShipment getProposedShipment() {
        return proposedShipment;
    }

    public MutableLiveData<Boolean> getProposal() {
        return proposal;
    }

    public void proposalDecline(){
        proposedPosition = -1;
        proposedShipment = new DriversShipment();
        proposal.postValue(false);

    }

    public void proposalTaken(){
        shipments.remove(proposedShipment.getShipment());
        if( drivers.getValue() != null){
            drivers.getValue().get(proposedPosition).setShipment(proposedShipment.getShipment() );
            drivers.getValue().get(proposedPosition).setSs(proposedShipment.getSs() );
            drivers.getValue().get(proposedPosition).setRoute(true );

            updatePosition.postValue(proposedPosition);

            proposalDecline();
        }
    }

    public MutableLiveData<Integer> getUpdatePosition() {
        return updatePosition;
    }

    public MutableLiveData<String> getMsjDialog() {
        return msjDialog;
    }
}
