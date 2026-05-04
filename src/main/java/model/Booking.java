package model;

import java.sql.Date;

public class Booking 
{
    private int bookingId;
    private String fullName, phone, email, address;
    private int serviceId;
    private String carType, carModel, carColor, bookingTime, notes, status;
    private Date bookingDate;

    // Constructor for creating new bookings
    public Booking(String fullName, String phone, String email, String address, 
    		int serviceId, String carType, String carModel, String carColor, 
    		Date bookingDate, String bookingTime, String notes) 
    {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.serviceId = serviceId;
        this.carType = carType;
        this.carModel = carModel;
        this.carColor = carColor;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.notes = notes;
        this.status = "Pending";
    }

    // Full constructor (for reading from DB)
    public Booking(int bookingId, String fullName, String phone, String email,
                   String address, int serviceId, String carType, String carModel,
                   String carColor, Date bookingDate, String bookingTime,
                   String notes, String status) 
    {
        this(fullName, phone, email, address, serviceId, carType, carModel,
             carColor, bookingDate, bookingTime, notes);
        this.bookingId = bookingId;
        this.status = status;
    }

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public int getServiceId() { return serviceId; }
    public String getCarType() { return carType; }
    public String getCarModel() { return carModel; }
    public String getCarColor() { return carColor; }
    public Date getBookingDate() { return bookingDate; }
    public String getBookingTime() { return bookingTime; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
    
    public void setStatus(String status) { this.status = status; }
}