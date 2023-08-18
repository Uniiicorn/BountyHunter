package com.cfunicorn.bountyhunter.utils;

import java.util.UUID;

public class Bounty {

    private UUID target, issuer;
    private int bounty, id;
    private String timeStamp;

    /**
     *
     * @param target UUID
     * @param issuer UUID
     * @param bounty int
     * @param id int
     * @param timeStamp String <br> <br>
     *
     * Bounty object, used to store and access data
     * should either be accessed by the target UUID or the general id
     */
    public Bounty(UUID target, UUID issuer, int bounty, int id, String timeStamp) {
        setTarget(target);
        setIssuer(issuer);
        setBounty(bounty);
        setId(id);
        setTimeStamp(timeStamp);
    }

    public UUID getTarget() {
        return target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    public UUID getIssuer() {
        return issuer;
    }

    public void setIssuer(UUID issuer) {
        this.issuer = issuer;
    }

    public int getBounty() {
        return bounty;
    }

    public void setBounty(int bounty) {
        this.bounty = bounty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
