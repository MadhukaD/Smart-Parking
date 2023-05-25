package com.example.smartparking;

public class BookingClass {
    private String Name;
    private String Contact;
    private String Slot;
    private String Date;
    private String Duration;

    public BookingClass(String name, String contact, String slot, String date, String duration) {
        Name = name;
        Contact = contact;
        Slot = slot;
        Date = date;
        Duration = duration;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getSlot() {
        return Slot;
    }

    public void setSlot(String slot) {
        Slot = slot;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }
}
