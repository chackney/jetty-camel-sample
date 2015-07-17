package org.muckingabout.model;

import java.util.Date;

/**
 * Created by CLHACKNE on 16/07/2015.
 */
public class Order {

    private String id;
    private String petName;
    private Date purchaseDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
