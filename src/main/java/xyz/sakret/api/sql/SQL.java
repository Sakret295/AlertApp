package xyz.sakret.api.sql;

import xyz.sakret.api.donation.entity.Donation;
import xyz.sakret.core.CFG;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Author: Starikov Vyacheslav
 * Department: 8011
 * Date: 18.01.19 14:30
 */
public interface SQL {
    final static public String  SITE_TWITCH = "Twitch";

    //Методы для работы с таблицей донатеров
    final static public String  ADD_DONATION = "INSERT INTO " + CFG.getSCHEMA() + ".\"Donation\" (donor, message, amount, currency, date. is_test) VALUES (?,?,?,?,?,?)";
    final static public String  GET_LAST_DONATION_ = "SELECT TOP 1 * FROM " + CFG.getSCHEMA() + ".\"Donation\" WHERE date <= ? and date >= ? ORDER BY id DESC";
    final static public String  GET_TOP_DONATION = "SELECT TOP 1 * FROM " + CFG.getSCHEMA() + "\"Donation\" WHERE date <= ? and date >= ? ORDER BY amount DESC";
    final static public String  GET_ALL_DONATION = "SELECT * FROM " + CFG.getSCHEMA() + "\"Donation\" ORDER BY id";
    final static public String  GET_ALL_DONATION_FROM_DONOR = "SELECT * FROM " + CFG.getSCHEMA() + "\"Donation\" WHERE donor = ? ";
    final static public String  GET_ALL_DONATION_FROM_TIME = "SELECT * FROM " + CFG.getSCHEMA() + "\"Donation\" WHERE date <= ? and date >= ? ";



    Connection getConnection();
    void initDataBase(Map<String, String> config);
    void closeConnection();
    boolean isConnect();

    //Донатерская таблица
    void addDonation(Donation donation);
    void getLastDonation(Timestamp start, Timestamp end);
    void getTopDonation(Timestamp start, Timestamp end);
    List<Donation> getAllDonation();
    List<Donation> getAllDonationFromDonor(String donor);
    List<Donation> getAllDonationFromTime(Timestamp start, Timestamp end);


}
