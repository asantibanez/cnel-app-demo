package com.andressantibanez.android.realmeventstore;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmEvent extends RealmObject {

    @PrimaryKey
    private String id;

    private long time;
    private boolean processed;

    private String owner;
    private String type;
    private String payload;

    public RealmEvent() {}

    public RealmEvent(String owner, String type, String payload) {
        id = UUID.randomUUID().toString();
        time = new Date().getTime();
        processed = false;

        this.owner = owner;
        this.type = type;
        this.payload = payload != null ? payload : "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
