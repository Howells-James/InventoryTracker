package nz.co.greenjersey.inventorytracker;

/**
 * Created by james on 30/01/2018.
 */

/**
 * A helper class to hold information about bookings
 * call create to create a booking
 * Used to temp hold booking before they are displayed
 */
public class Booking{
    public String fName;
    public String lName;
    public String bookingId;
    public Booking(String fName, String lName, String bookingId){
        this.fName = fName;
        this.lName = lName;
        this.bookingId = bookingId;
    }

}