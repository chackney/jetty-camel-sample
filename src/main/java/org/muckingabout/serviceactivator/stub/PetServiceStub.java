package org.muckingabout.serviceactivator.stub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.muckingabout.model.Order;
import org.muckingabout.model.Pet;

/**
 * @author chackney
 */
public class PetServiceStub {



    /**
     * Gets a user by the given id
     *
     * @param id  the id of the user
     * @return the user, or <tt>null</tt> if no user exists
     */
    public Pet getPet(String id) {
        return new Pet(123,"Stubby The Cat");
    }

    /**
     * List all pets
     *
     * @return the list of all pets
     */
    public Collection<Pet> listPets()
    {
        return new ArrayList<Pet>();
    }

    /**
     * Updates or creates the given user
     *
     * @param user the user
     */
    public void updatePet(Pet user) {
        System.out.println("update pets");
    }

    public Collection<Order> listOrders(String id)  {
        return null;
    }

   public void placeOrder(Order order) {
       System.out.println("order placed pets");
   }

}

