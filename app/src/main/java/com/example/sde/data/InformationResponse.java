package com.example.sde.data;

import java.util.ArrayList;

public class InformationResponse {
    private ArrayList<String> shipments;
    private ArrayList<String> drivers;

    public ArrayList<String> getShipments() {
        return shipments;
    }

    public void setShipments(ArrayList<String> shipments) {
        this.shipments = shipments;
    }

    public ArrayList<String> getDrivers() {
        return drivers;
    }

    public void setDrivers(ArrayList<String> drivers) {
        this.drivers = drivers;
    }
}
