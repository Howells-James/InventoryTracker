package nz.co.greenjersey.inventorytracker.Misc;

import java.util.Date;

/**
 * Created by james on 10/04/2018.
 */

public class Bike {

    private String tagNumber;
    private Date dateAdded;
    private String idNum;
    private String brand;
    private String color;
    private String desc;

    @Override
    public String toString() {
        return "Bike{" +
                "tagNumber='" + tagNumber + '\'' +
                ", dateAdded=" + dateAdded +
                ", idNum='" + idNum + '\'' +
                ", brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public Bike(String tagNumber, Date dateAdded, String idNum, String brand, String color, String desc) {
        this.tagNumber = tagNumber;
        this.dateAdded = dateAdded;
        this.idNum = idNum;
        this.brand = brand;
        this.color = color;
        this.desc = desc;
    }

    private Bike(){}



    public void setIdNum(String idNum) {
        this.idNum = idNum;
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

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }


    public String getIdNum() {
        return idNum;
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

    public String getTagNumber() {
        return tagNumber;
    }

    public Date getDateAdded() {
        return dateAdded;
    }


}
