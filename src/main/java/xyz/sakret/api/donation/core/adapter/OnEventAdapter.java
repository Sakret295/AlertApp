package xyz.sakret.api.donation.core.adapter;

import com.google.common.eventbus.Subscribe;
import org.json.JSONObject;
import xyz.sakret.api.donation.entity.Donation;

/**
 * Author: Starikov Vyacheslav
 * Department: 8011
 * Date: 18.01.19 12:23
 */
public abstract class OnEventAdapter {

    @Subscribe
    public abstract void onDonation(Donation donation);

    @Subscribe
    public abstract void onDonation(JSONObject timeout);
}
