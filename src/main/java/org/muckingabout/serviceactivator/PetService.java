package org.muckingabout.serviceactivator;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.muckingabout.model.Order;
import org.muckingabout.model.Pet;

/**
 * @author chackney
 */
public class PetService {

    // use a tree map so they become sorted
    private final Map<String, Pet> pets = new TreeMap<String, Pet>();

    // use a tree map so they become sorted
    private final Map<String, Order> orders = new TreeMap<String, Order>();

    private Random ran = new Random();

    public PetService() {
        pets.put("123", new Pet(123, "Cat"));
        pets.put("456", new Pet(456, "Donald Duck"));
        pets.put("789", new Pet(789, "Micky Mouse"));
    }

    /**
     * Gets a user by the given id
     *
     * @param id  the id of the user
     * @return the user, or <tt>null</tt> if no user exists
     */
    public Pet getPet(String id) {
        if ("789".equals(id)) {
            // simulate some cpu processing time when returning the slow turtle
            int delay = 500 + ran.nextInt(1500);
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                // ignore
            }
        }
        return pets.get(id);
    }

    /**
     * List all pets
     *
     * @return the list of all pets
     */
    public Collection<Pet> listPets()
    {
        return pets.values();
    }

    /**
     * Updates or creates the given user
     *
     * @param user the user
     */
    public void updatePet(Pet user) {
        pets.put("" + user.getId(), user);
    }

    public Collection<Order> listOrders(String id)  {
        return orders.values();
    }

   public void placeOrder(Order order) {
       orders.put("00012-" + ran.nextLong(), order);
   }

}

