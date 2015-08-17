package com.andressantibanez.android.realmeventstore;

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RealmEventStore {

    private static RealmConfiguration configuration = null;

    public static void init(Context context) {
        if(configuration == null) {
            configuration = new RealmConfiguration.Builder(context).build();
            Realm.setDefaultConfiguration(configuration);
        }
    }

    private static void push(RealmEvent event) {
        Realm realm = RealmEventStore.getRealm();

        realm.beginTransaction();
        realm.copyToRealm(event);
        realm.commitTransaction();
    }

    private static Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    /**
     * Public API
     */
    //Push without Payload
    public static void push(String owner, String type) {
        RealmEvent event = new RealmEvent(owner, type, "");
        RealmEventStore.push(event);
    }

    //Push with Payload
    public static void push(String owner, String type, String payload) {
        RealmEvent event = new RealmEvent(owner, type, payload);
        RealmEventStore.push(event);
    }

    //Mark As Processed
    public static void markAsProcessed(RealmEvent event) {
        try {
            Realm realm = getRealm();
            realm.beginTransaction();

            event.setProcessed(true);

            realm.copyToRealmOrUpdate(event);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Set Listener
    public static Realm setListener(RealmChangeListener listener) {
        Realm realm = getRealm();
        realm.addChangeListener(listener);
        return realm;
    }

    //Remove Listener
    public static void removeListener(Realm realm, RealmChangeListener listener) {
        realm.removeChangeListener(listener);
        realm.close();
    }

    //Count
    public static int getPendingEventsCount(String owner) {
        Realm realm = RealmEventStore.getRealm();

        RealmQuery<RealmEvent> query = realm.where(RealmEvent.class);
        query.equalTo("owner", owner);
        query.equalTo("processed", false);
        return query.findAll().size();
    }

    public static ArrayList<RealmEvent> getPendingEvents(String owner) {
        Realm realm = RealmEventStore.getRealm();

        RealmQuery<RealmEvent> query = realm.where(RealmEvent.class);
        query.equalTo("owner", owner);
        query.equalTo("processed", false);
        RealmResults<RealmEvent> result = query.findAll();

        ArrayList<RealmEvent> pendingEvents = new ArrayList<>();
        for(RealmEvent event : result) {
            pendingEvents.add(event);
        }

        return pendingEvents;
    }
}
