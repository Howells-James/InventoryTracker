package nz.co.greenjersey.inventorytracker.Objects;

import java.util.ArrayList;

/**
 * Created by james on 12/04/2018.
 */

public class Customer {

    String fName;


    public void addBike(String s){
        bikes.add(s);
    }
    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setBikes(ArrayList<String> bikes) {
        this.bikes = bikes;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getBookingId() {
        return bookingId;
    }

    public ArrayList<String> getBikes() {
        if(bikes != null) {
            return bikes;
        }
        init();
        return bikes;
    }

    String lName;

    @Override
    public String toString() {
        return "Customer{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", bikes=" + bikes +
                '}';
    }

    String bookingId;
    ArrayList<String> bikes;

    public Customer(String fName, String lName, String bookingId, ArrayList<String> bikes) {
        this.fName = fName;
        this.lName = lName;
        this.bookingId = bookingId;
        this.bikes = bikes;

    }

    public Customer(){
    }

    public void init(){
        bikes = new ArrayList<>();
    }

    public void removeBike(String id){
        bikes.remove(id);
    }
}
