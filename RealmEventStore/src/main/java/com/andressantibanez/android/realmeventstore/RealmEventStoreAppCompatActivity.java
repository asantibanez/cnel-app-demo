package com.andressantibanez.android.realmeventstore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class RealmEventStoreAppCompatActivity extends AppCompatActivity implements RealmChangeListener {

    private static final String REALM_EVENT_STORE_TAG = "RealmEventStore";

    private static final String REALM_ACTIVITY_ID = "realm_activity_id";

    private Realm mRealm;
    private String mRealmActivityId;
    private boolean mListeningToEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RealmEventStore.init(this);

        if(savedInstanceState != null)
            mRealmActivityId = savedInstanceState.getString(REALM_ACTIVITY_ID);
        else
            mRealmActivityId = UUID.randomUUID().toString();

        mListeningToEvents = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(REALM_ACTIVITY_ID, mRealmActivityId);
    }

    public String getRealmActivityId() {
        return mRealmActivityId;
    }

    @Override
    protected void onPause() {
        stopListeningForEvents();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startListeningForEvents();
    }

    private synchronized void startListeningForEvents() {
        Log.e(REALM_EVENT_STORE_TAG, "Start Listening");

        mRealm = RealmEventStore.setListener(this);
        mListeningToEvents = true;

        if(RealmEventStore.getPendingEventsCount(mRealmActivityId) > 0)
            processPendingEvents();
    }

    private synchronized void stopListeningForEvents() {
        Log.e(REALM_EVENT_STORE_TAG, "Stop Listening");

        RealmEventStore.removeListener(mRealm, this);
        mListeningToEvents = false;
    }

    private synchronized void processPendingEvents() {
        //Check if listening for events
        if(!mListeningToEvents) return;

        //Stop listening to events. Realm calls onChanged on every write
        stopListeningForEvents();

        //Get pending events for owner
        ArrayList<RealmEvent> events = RealmEventStore.getPendingEvents(mRealmActivityId);
        Log.i(REALM_EVENT_STORE_TAG, "Pending: " + events.size());

        //Process each pending event
        for (RealmEvent event : events) {
            processEvent(event.getId(), event.getType(), event.getPayload());
            RealmEventStore.markAsProcessed(event);
        }

        //Start listening for events
        startListeningForEvents();
    }

    public void processEvent(String id, String type, String payload) {
        Log.d(REALM_EVENT_STORE_TAG, id + ":" + type + ":" + payload);
    }

    @Override
    public void onChange() {
        processPendingEvents();
    }
}
