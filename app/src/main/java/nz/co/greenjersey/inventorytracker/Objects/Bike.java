package nz.co.greenjersey.inventorytracker.Objects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by james on 10/04/2018.
 */

public class Bike {

    private Long tagNumber;
    private Date dateAdded;
    private String serialNumber;
    private String brand;
    private String color;

    @Override
    public String toString() {
        return "Bike{" +
                "tagNumber=" + tagNumber +
                ", dateAdded=" + dateAdded +
                ", serialNumber='" + serialNumber + '\'' +
                ", brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                ", desc='" + desc + '\'' +
                ", location='" + location + '\'' +
                ", inUse=" + inUse +
                ", numUses=" + numUses +
                ", currentOrder='" + currentOrder + '\'' +
                ", gjStickerNo='" + gjStickerNo + '\'' +
                ", mainDescs=" + mainDescs +
                '}';
    }

    private String desc;
    private  String location;
    private boolean inUse;
    private int numUses;
    private String currentOrder;
    private String gjStickerNo;
    private ArrayList<String> mainDescs;

    public void addMainDesc(String md){
        mainDescs.add(md);
    }

    public void init(){
        mainDescs = new ArrayList<>();
    }

    public ArrayList<String> getMainDescs() {
        return mainDescs;
    }

    public void setMainDescs(ArrayList<String> mainDescs) {
        this.mainDescs = mainDescs;
    }

    public String getGjStickerNo() {
        return gjStickerNo;
    }

    public void setGjStickerNo(String gjStickerNo) {
        this.gjStickerNo = gjStickerNo;
    }

    public String getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(String currentOrder) {
        this.currentOrder = currentOrder;
    }

    public int getNumUses() {
        return numUses;
    }

    public void setNumUses(int numUses) {
        this.numUses = numUses;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }



    public Bike(Long tagNumber, Date dateAdded, String serialNumber, String brand, String color, String desc, String location, boolean inUse, int numUses, String currentOrder, String gjStickerNo, ArrayList<String> md) {
        this.tagNumber = tagNumber;
        this.dateAdded = dateAdded;
        this.serialNumber = serialNumber;
        this.brand = brand;
        this.color = color;
        this.desc = desc;
        this.location = location;
        this.inUse = inUse;
        this.currentOrder = currentOrder;
        this.gjStickerNo = gjStickerNo;
        if(md.size() == 0){
            mainDescs = new ArrayList<>();
        }
        else {
            this.mainDescs = md;
        }
    }

    private Bike(){}



    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTagNumber(Long tagNumber) {
        this.tagNumber = tagNumber;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }


    public String getSerialNumber() {
        return serialNumber;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getDesc() {
        return desc;
    }

    public Long getTagNumber() {
        return tagNumber;
    }

    public Date getDateAdded() {
        return dateAdded;
    }


}
