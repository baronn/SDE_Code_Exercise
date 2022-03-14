package com.example.sde.data;

public class DriversShipment {
    private String driver;
    private String shipment;
    private double ss;
    private boolean route;

    public DriversShipment() {
        this.driver = "";
        this.shipment = "";
        this.route = false;
        this.ss= 0.0;
    }

    public DriversShipment(String driver) {
        this.driver = driver;
        this.shipment = "";
        this.route = false;
        this.ss= 0.0;

    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public boolean isRoute() {
        return route;
    }

    public void setRoute(boolean route) {
        this.route = route;
    }

    public double getSs() {
        return ss;
    }

    public void setSs(double ss) {
        this.ss = ss;
    }
}
