package edu.bu.mad.jiayao.bumad2016_jiayao;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import java.util.ArrayList;

@DynamoDBTable(tableName = "Users")
public class User {
    private String username;
    private String email;
    private String password;
    private ArrayList<String> listings;
    private ArrayList<ArrayList<String>> reservations;
    private ArrayList<ArrayList<String>> notifications;
    private ArrayList<ArrayList<String>> bookmarks;

    @DynamoDBHashKey(attributeName = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DynamoDBAttribute(attributeName = "listings")
    public ArrayList<String> getListings() {
        return listings;
    }

    public void setListings(ArrayList<String> listings) {
        this.listings = listings;
    }

    @DynamoDBAttribute(attributeName = "reservations")
    public ArrayList<ArrayList<String>> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<ArrayList<String>> reservations) {
        this.reservations = reservations;
    }

    @DynamoDBAttribute(attributeName = "notifications")
    public ArrayList<ArrayList<String>> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<ArrayList<String>> notifications) {
        this.notifications = notifications;
    }

    @DynamoDBAttribute(attributeName = "bookmarks")
    public ArrayList<ArrayList<String>> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(ArrayList<ArrayList<String>> bookmarks) {
        this.bookmarks = bookmarks;
    }
}
