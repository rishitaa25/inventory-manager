package com.example.androidexample.features.notifications.model;

import com.example.androidexample.features.user.enums.AccessLevel;

import java.time.LocalDate;

public class NotificationObject {

    private Integer id;

    private AccessLevel recipient;

    private String message;

    private LocalDate date;

    private Status status;

    private Integer requestedByEmployeeId;

    private String requesterType;

    private String firstName;

    private String lastName;

    private String userName;

    private String email;

    private String phoneNumber;

    private String driver;

    public NotificationObject(Integer id, AccessLevel recipient, String message, LocalDate date, Status status, Integer requestedByEmployeeId,
                              String requesterType, String firstName, String lastName, String userName, String email, String phoneNumber, String driver){
        this.id = id;
        this.recipient = recipient;
        this.message = message;
        this.date = date;
        this.status = status;
        this.requestedByEmployeeId = requestedByEmployeeId;
        this.requesterType = requesterType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.driver = driver;
    }

    public String getTitle()
    {
        return userName + " " + requesterType;
    }

    public String getDate() {
        return date != null ? date.toString() : "N/A";
    }

    public Integer getId()
    {
        return id;
    }

    public AccessLevel getRecipient()
    {
        return recipient;
    }

    public String getMessage()
    {
        return message;
    }

//    public LocalDate getDate()
//    {
//        return date;
//    }

    public Status getStatus()
    {
        return status;
    }

    public Integer getRequestedByEmployeeId()
    {
        return requestedByEmployeeId;
    }

    public String getRequesterType()
    {
        return requesterType;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getDriver()
    {
        return driver;
    }
    public int getNotificationId() {
        return id;
    }

}
