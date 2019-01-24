package xyz.sakret.api.donation.core;

import com.google.common.eventbus.EventBus;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.sakret.api.donation.core.adapter.OnEventAdapter;
import xyz.sakret.api.donation.entity.Currency;
import xyz.sakret.api.donation.entity.Donation;

import java.net.URI;

/**
 * Author: Starikov Vyacheslav
 * Department: 8011
 * Date: 18.01.19 12:25
 */
public class DonationAlertsCore {
    private static final Logger log = LogManager.getLogger(DonationAlertsCore.class);
    private static DonationAlertsCore instance;

    private final static URI SOCKET_IO_URL = URI.create("http://socket.donationalerts.ru:3001");
    private Socket socket;

    private boolean reconnection = true;
    private int reconnectionAttempts = 5;
    private long reconnectionDelay = 5000L;

    private final EventBus eventBus = new EventBus();

    private DonationAlertsCore(String accessToken) {
        JSONObject authStruct = makeAuthStruct(accessToken);

        socket = IO.socket(SOCKET_IO_URL);

        socket.io().reconnection(reconnection);
        socket.io().reconnectionAttempts(reconnectionAttempts);
        socket.io().reconnectionDelay(reconnectionDelay);

        socket.emit("add-user", authStruct);
        socket.on("reconnect", args -> socket.emit("add-user", authStruct));

        EventListener eventListener = new EventListener(eventBus);
        socket.on("donation", eventListener::onDonation);
        socket.on("connect_timeout", eventListener::onTimeout);
    }

    public static synchronized DonationAlertsCore getInstance(String accessToken) {
        if (instance == null)
            instance = new DonationAlertsCore(accessToken);

        return instance;
    }

    private JSONObject makeAuthStruct(String accessToken) {
        JSONObject authStruct = new JSONObject();

        try {
            authStruct.put("type", "alert_widget");
            authStruct.put("token", accessToken);
        } catch (JSONException e) {
            log.error(e);
        }

        return authStruct;
    }

    public boolean isReconnection() {
        return reconnection;
    }

    public void setReconnection(boolean reconnection) {
        this.reconnection = reconnection;
    }

    public int getReconnectionAttempts() {
        return reconnectionAttempts;
    }

    public void setReconnectionAttempts(int reconnectionAttempts) {
        this.reconnectionAttempts = reconnectionAttempts;
    }

    public long getReconnectionDelay() {
        return reconnectionDelay;
    }

    public void setReconnectionDelay(long reconnectionDelay) {
        this.reconnectionDelay = reconnectionDelay;
    }

    public void addEvent(OnEventAdapter donationAdapter) {
        eventBus.register(donationAdapter);
    }

    public void removeEvent(OnEventAdapter donationAdapter) {
        eventBus.unregister(donationAdapter);
    }

    public boolean isConnected() {
        return socket.connected();
    }

    public void connect() {
        socket.connect();
    }

    public void disconnect() {
        if (isConnected()) {
            socket.disconnect();
            socket.close();
        }
    }

}

class EventListener {
    private static final Logger log = LogManager.getLogger(EventListener.class);

    private final EventBus eventBus;

    EventListener(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    void onDonation(Object[] args) {
        JSONObject donationJsonStruct = parseJsonStruct(args[0].toString());
        Donation donation = new Donation();
        try {
            donation.setDonor(donationJsonStruct.get("username").toString());
            donation.setMessage(donationJsonStruct.get("message").toString());
            donation.setAmount(Double.parseDouble(donationJsonStruct.get("amount").toString()));
            donation.setCurrency(Currency.valueOf(donationJsonStruct.get("currency").toString()));
            donation.setDate(donationJsonStruct.get("date_paid").toString());
        } catch (JSONException e) {
            log.error(e);
        }

        eventBus.post(donation);
    }

    void onTimeout(Object[] args) {
        eventBus.post(parseJsonStruct(args[0].toString()));
    }

    private JSONObject parseJsonStruct(String arg) {
        try {
            return new JSONObject(arg);
        } catch (JSONException e) {
            log.error(e);
            return null;
        }
    }
}
