package xyz.sakret.api.donation.entity;

import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Author: Starikov Vyacheslav
 * Department: 8011
 * Date: 18.01.19 12:12
 */
public class Donation {
    private StringProperty donor = new SimpleStringProperty(this, "donor", "Аноним");
    private StringProperty message = new SimpleStringProperty(this, "message", "Unknown");
    private DoubleProperty amount = new SimpleDoubleProperty(this, "amount", 0.0);
    private Currency currency;
    private LocalDateTime date;
    private boolean testAlerts;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss");

    public Donation() {

    }

    public Donation(String donor, String message, Double amount, Currency currency, LocalDateTime date, boolean testAlerts) {
        this.donor.setValue(donor);
        this.message.setValue(message);
        this.amount.setValue(amount);
        this.currency = currency;
        this.date = date;
        this.testAlerts = testAlerts;

    }

    //-----------------------Getter And Setter-------------------------

    public String getDonor() {
        return donor.get();
    }

    public StringProperty donorProperty() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor.set(donor);
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public double getAmount() {
        return amount.get();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = LocalDateTime.parse(date, dtf);
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isTestAlerts() {
        return testAlerts;
    }

    public void setTestAlerts(boolean testAlerts) {
        this.testAlerts = testAlerts;
    }
}
