/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpa2reps;

/**
 *
 * @author lenovo
 */
public class BPA2REPS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
}

class Area {

    private int id;
    private String name;
    
    public Area(int id, String name){
        this.id=id;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Zone {

    private int zoneId;
    private String name;
    private int areaId;
    
    public Zone(int zoneId, int areaId, String name){
        this.zoneId = zoneId;
        this.areaId = areaId;
        this.name   = name;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }
}

class Generator {
    
    private int id;
    private String busName;
    private double baseKV;
    private int zoneId;
    private int areaId;
    private double rateMW;
    private String unitType;
    
    public Generator(int id, String busName, double baseKV, int zoneId, int areaId, double rateMW, String unitType){
        this.id = id;
        this.busName = busName;
        this.baseKV = baseKV;
        this.zoneId = zoneId;
        this.areaId = areaId;
        this.rateMW = rateMW;
        this.unitType = unitType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public double getBaseKV() {
        return baseKV;
    }

    public void setBaseKV(double baseKV) {
        this.baseKV = baseKV;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public double getRateMW() {
        return rateMW;
    }

    public void setRateMW(double rateMW) {
        this.rateMW = rateMW;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
}

class Tieline {
    
    private int id;
    private int areaId1;
    private int areaId2;
    private String busName1;
    private String busName2;
    private double baseKV;
    
    public Tieline(int id, int areaId1, int areaId2, String busName1, String busName2, double baseKV){
        this.id = id;
        this.areaId1 = areaId1;
        this.areaId2 = areaId2;
        this.busName1 = busName1;
        this.busName2 = busName2;
        this.baseKV = baseKV;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getAreaId1() {
        return areaId1;
    }

    public void setAreaId1(int areaId1) {
        this.areaId1 = areaId1;
    }
    
    public int getAreaId2() {
        return areaId2;
    }

    public void setAreaId2(int areaId2) {
        this.areaId2 = areaId2;
    }
    
    public String getBusName1() {
        return busName1;
    }

    public void setBusName1(String busName1) {
        this.busName1 = busName1;
    }    
    
    public String getBusName2() {
        return busName2;
    }

    public void setBusName2(String busName2) {
        this.busName2 = busName2;
    }
    
    public double getBaseKV() {
        return baseKV;
    }

    public void setBaseKV(double baseKV) {
        this.baseKV = baseKV;
    }
}
