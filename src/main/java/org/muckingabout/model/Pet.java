package org.muckingabout.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Represents an pet on the system")
public class Pet {

    private int id;
    private String name;

    public Pet() {
    }

    public Pet(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @ApiModelProperty(value = "The id of the pet", required = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ApiModelProperty(value = "The name of the pet", required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}