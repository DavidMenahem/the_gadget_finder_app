package com.david.thegadjetfinder.model;

public class Gadget {
    private Long id;
    private Long userID;
    private String gadget;

    public Gadget(Long userID, String gadget) {
        this.userID = userID;
        this.gadget = gadget;
    }

    public Gadget(Long id, Long userID, String gadget) {
        this.id = id;
        this.userID = userID;
        this.gadget = gadget;
    }


    public String getGadget() {
        return gadget;
    }

    public Long getId() {
        return id;
    }
}
