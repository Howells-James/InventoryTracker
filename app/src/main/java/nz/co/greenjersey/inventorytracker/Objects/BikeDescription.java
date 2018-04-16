package nz.co.greenjersey.inventorytracker.Objects;

/**
 * Created by james on 10/04/2018.
 */

public class BikeDescription {

    public void setBrand(String brand) {
        this.brand = brand;
    }

    private String brand;
    private String color;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private String desc;

 private BikeDescription(){}

 public BikeDescription(String desc, String col, String brand){
     this.desc = desc;
     this.color = col;
     this.brand = brand;
 }

    public String getDesc(){
         return desc;
    }
    public String getColor() {
        return color;
    }
    public String getBrand() {
        return brand;
    }
}
