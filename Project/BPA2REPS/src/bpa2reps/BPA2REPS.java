/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpa2reps;

import BPAcards.*;
import java.io.File;
import java.util.*;
import jxl.Workbook;     
import jxl.write.Label;     
import jxl.write.WritableSheet;     
import jxl.write.WritableWorkbook;   

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
        Scanner scan = new Scanner(System.in);
        Grid bpaGrid= new Grid();
        System.out.println("输入BPA文件名称");
        String bpaFile = scan.next();
        System.out.println("输入结果文件名称");
        String result = scan.next();   
        bpaGrid.CreateGridFromBPA(bpaFile);
        try {
            //  打开文件      
            WritableWorkbook book = Workbook.createWorkbook(new File(result ));
            //  生成名为“第一页”的工作表，参数0表示这是第一页      
            WritableSheet areaSheet = book.createSheet("area", 0);
            WritableSheet zoneSheet = book.createSheet("zone", 1);
            WritableSheet generatorSheet = book.createSheet("generator", 2);
            WritableSheet tielineSheet = book.createSheet("tieline", 3);
            
            jxl.write.Number number;
            Label label;
            // area sheet
            label = new Label(0,0,"序号");
            areaSheet.addCell(label);
            label = new Label(1,0,"区域名称");
            areaSheet.addCell(label);
            int row=1;        
            for (Area area :bpaGrid.areaTable){
                number = new jxl.write.Number(0, row, area.getId());
                areaSheet.addCell(number);
                label = new Label(1, row, area.getName());
                areaSheet.addCell(label);
                row++;
            }
            
            // zone sheet
            label = new Label(0,0,"分区编号");
            zoneSheet.addCell(label);
            label = new Label(1,0,"区域编号");
            zoneSheet.addCell(label);
            label = new Label(2,0,"分区名称");
            zoneSheet.addCell(label);
            row=1;        
            for (Zone zone :bpaGrid.zoneTable){
                number = new jxl.write.Number(0, row, zone.getZoneId());
                zoneSheet.addCell(number);
                number = new jxl.write.Number(1, row, zone.getAreaId());
                zoneSheet.addCell(number);
                label = new Label(2, row, zone.getName());
                zoneSheet.addCell(label);
                row++;
            }
            
            // generator sheet
            label = new Label(0,0,"发电机编号");
            generatorSheet.addCell(label);
            label = new Label(1,0,"节点名");
            generatorSheet.addCell(label);
            label = new Label(2,0,"电压等级");
            generatorSheet.addCell(label);
            label = new Label(3,0,"分区编号");
            generatorSheet.addCell(label);
            label = new Label(4,0,"区域编号");
            generatorSheet.addCell(label);
            label = new Label(5,0,"额定容量");
            generatorSheet.addCell(label);
            label = new Label(6,0,"机组类型");
            generatorSheet.addCell(label);
            row=1;        
            for (Generator generator :bpaGrid.generatorTable){
                number = new jxl.write.Number(0, row, generator.getId());
                generatorSheet.addCell(number);
                label = new Label(1, row, generator.getBusName());
                generatorSheet.addCell(label);
                number = new jxl.write.Number(2, row, generator.getBaseKV());
                generatorSheet.addCell(number);
                number = new jxl.write.Number(3, row, generator.getZoneId());
                generatorSheet.addCell(number);
                number = new jxl.write.Number(4, row, generator.getAreaId());
                generatorSheet.addCell(number);
                number = new jxl.write.Number(5, row, generator.getRateMW());
                generatorSheet.addCell(number);
                label = new Label(6, row, generator.getUnitType());
                generatorSheet.addCell(label);
                row++;
            }
            
             // tieline sheet
            label = new Label(0,0,"序号");
            tielineSheet.addCell(label);
            label = new Label(1,0,"区域1编号");
            tielineSheet.addCell(label);
            label = new Label(2,0,"区域2编号");
            tielineSheet.addCell(label);
            label = new Label(3,0,"节点1名称");
            tielineSheet.addCell(label);
            label = new Label(4,0,"节点2名称");
            tielineSheet.addCell(label);
            label = new Label(5,0,"电压等级");
            tielineSheet.addCell(label);
            row=1;        
            for (Tieline tieline :bpaGrid.tielineTable){
                number = new jxl.write.Number(0, row, tieline.getId());
                tielineSheet.addCell(number);
                number = new jxl.write.Number(1, row, tieline.getAreaId1());
                tielineSheet.addCell(number);
                number = new jxl.write.Number(2, row, tieline.getAreaId2());
                tielineSheet.addCell(number);
                label = new Label(3, row, tieline.getBusName1());
                tielineSheet.addCell(label);
                label = new Label(4, row, tieline.getBusName2());
                tielineSheet.addCell(label);
                number = new jxl.write.Number(5, row, tieline.getBaseKV());
                tielineSheet.addCell(number);
                row++;
            }
            //  写入数据并关闭文件      
            book.write();
            book.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
class Grid{
    
    /**
     * bpagrid
     */
    public List<Area> areaTable;
    public List<Zone> zoneTable;
    public List<Generator> generatorTable;
    public List<Tieline> tielineTable;
    private TreeMap<BusNameKV, Integer> busNameKV_ID;
    private TreeMap<String, ZoneTableKey> zoneName_ID;
    
    public Grid(){
        areaTable = new ArrayList<>();
        zoneTable = new ArrayList<>();
        generatorTable = new ArrayList<>();
        tielineTable = new ArrayList<>();
        busNameKV_ID = new TreeMap<>();
        zoneName_ID = new TreeMap<>();        
    }
     public void CreateGridFromBPA(String bpaFile){
         GridBPA bpaGrid = new GridBPA();
         bpaGrid.transBPA(bpaFile);
         int areaID = 0, zoneID = 0, busID = 0, loadID = 0, genID = 0, lineID = 0, tieID = 0;
         for (ACcard acd : bpaGrid.accardArray) {
           areaID++;
            areaTable.add(new Area(areaID, acd.areaName));
            for (int i = 0; i < acd.zoneNo; i++) {
                zoneID++;
                zoneTable.add(new Zone(zoneID, areaID, acd.zone[i]));
                zoneName_ID.put(acd.zone[i], new ZoneTableKey(zoneID,areaID));
            }
        }
        areaTable.add(new Area(areaID + 1, "其他"));
        zoneTable.add(new Zone(zoneID + 1, areaID + 1, "其他"));
        zoneName_ID.put("其他", new ZoneTableKey(zoneID + 1,areaID + 1));
        
        for (Bcard bcd : bpaGrid.bcardArray) {
            busID++;
            ZoneTableKey bus_zone;   //bus的分区序号
            if (zoneName_ID.containsKey(bcd.zone)) {
                bus_zone = zoneName_ID.get(bcd.zone);
            } else {
                bus_zone = zoneName_ID.get("其他");
            }
            busNameKV_ID.put(new BusNameKV(bcd.name, bcd.kV), busID);
            if (isGenBus(bcd)) {
                genID++;
                generatorTable.add(new Generator(genID, bcd.name, bcd.kV, bus_zone.zoneID, bus_zone.areaID, bcd.Pmax, "未知"));
            }
        }
        for (Lcard lcd : bpaGrid.lcardArray) {
            lineID++;
            Bcard line_bus1 = bpaGrid.bcardArray.get(busNameKV_ID.get(new BusNameKV(lcd.name1, lcd.kV1)) - 1);
            Bcard line_bus2 = bpaGrid.bcardArray.get(busNameKV_ID.get(new BusNameKV(lcd.name2, lcd.kV2)) - 1);
         
            ZoneTableKey line_zone1 = zoneName_ID.get(line_bus1.zone);
            ZoneTableKey line_zone2 = zoneName_ID.get(line_bus2.zone);
            if (!line_zone1.equals(line_zone2) ) {
                tieID++;
                tielineTable.add(new Tieline(tieID, line_zone1.areaID, line_zone2.areaID, line_bus1.name, line_bus2.name, line_bus1.kV));
            } 
        }
     }
    private static boolean isLoadBus(Bcard bcd) {
        boolean flag = false;
        if (!isZero(bcd.PMW)) {
            flag = true;
        }
        return flag;
    }

    private static boolean isGenBus(Bcard bcd) {
        boolean flag = false;
        if (!(isZero(bcd.PgenMW) && (isZero(bcd.Pmax)))) {
            flag = true;
        }
        return flag;
    }

    private static boolean isZero(double f) {
        boolean flag = false;
        if ((f > -1e-5) && (f < 1e-5)) {
            flag = true;
        }
        return flag;
    }
}

class BusNameKV implements Comparable {

    String name;
    double basekV;
    private static double ekV = 0.05;		//基准电压 浮点型误差

    public BusNameKV(String name, double basekV) {
        this.name = name;
        this.basekV = basekV;
    }

    public double getekV() {
        return ekV;
    }

    public void setekV(double ekV) {
        BusNameKV.ekV = ekV;
    }

    @Override
    public int compareTo(Object obj) {
        BusNameKV bus = (BusNameKV) obj;
        int flag = 0;
        if (this.name.compareTo(bus.name) > 0) {
            flag = 1;
        } else if (this.name.compareTo(bus.name) < 0) {
            flag = -1;
        } else {
            if ((this.basekV - bus.basekV) > ekV) {
                flag = 1;
            } else if ((this.basekV - bus.basekV) < -ekV) {
                flag = -1;
            }
        }
        return flag;
    }
}

class ZoneTableKey implements Comparable {

    int zoneID;
    int areaID;

    public ZoneTableKey(int zoneID, int areaID) {
        this.zoneID = zoneID;
        this.areaID = areaID;
    }
    @Override
    public int compareTo(Object obj) {
        ZoneTableKey zone = (ZoneTableKey) obj;
        int flag = 0;
        if (this. areaID > zone.areaID) {
            flag = 1;
        } else if (this.areaID < zone.areaID) {
            flag = -1;
        } else {
            if (this.zoneID > zone.zoneID) {
                flag = 1;
            } else if (this.zoneID < zone.zoneID){
                flag = -1;
            }
        }
        return flag;
    }
    public boolean equals(ZoneTableKey zone) {
        boolean flag = false;
        if ((this. areaID == zone.areaID) && ((this.zoneID == zone.zoneID))){
            flag = true;
        }
        return flag;
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
