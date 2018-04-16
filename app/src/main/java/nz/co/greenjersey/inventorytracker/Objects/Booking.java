package nz.co.greenjersey.inventorytracker.Objects;

/**
 * Created by james on 30/01/2018.
 */

/**
 * A helper class to hold information about bookings
 * call create to create a booking
 * Used to temp hold booking before they are displayed
 * A booking differs from a customer in that a booking has not been and will not be assigned bikes
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