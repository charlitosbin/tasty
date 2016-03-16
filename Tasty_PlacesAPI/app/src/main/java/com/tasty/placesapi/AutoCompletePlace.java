package com.tasty.placesapi;

/**
 * Tasty_PlacesAPI for Distributed Systems Project  EEEV  11/03/2016
 */
/**
 * Representación de un lugar desde la llamada de Autocomplete
 */
public class AutoCompletePlace {

    private String id;
    private String description;

    public AutoCompletePlace( String id, String description ) {
        this.id = id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }
}
