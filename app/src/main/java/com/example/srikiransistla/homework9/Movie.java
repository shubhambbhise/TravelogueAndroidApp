package com.example.srikiransistla.homework9;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@JsonIgnoreProperties({"selection"})
public class Movie implements Serializable {

    String name, description, id, primaryID, image, stars, url, year, length, director;
    //float rating;
    ArrayList<HashMap<String, Object>> Hotel = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, Object>> Restaurants = new ArrayList<HashMap<String, Object>>();
    ArrayList<Object> PlacesVisited =
            new ArrayList<Object>();


    /* Recycler View components
     1. id - syracuse-username;
     2. description - wow its a great place;
     3. image - display pic
     4. name - syracuse
     5. primaryID - unique counter
     6. stars - date in string format 01-Apr-2016

     Detail components
     7. Hotel - Arraylist<HashMap Hotels>
     8. Restaurants - ArrayList<HashMap Restaurants>
     9. PlacesVisited - ArrayList PlacesVisited<HashMap Place(SU)<HashMap Images,Location,Videos>>>
     */

    public Movie() {
    }

    public ArrayList<HashMap<String, Object>> getHotel() {
        return Hotel;
    }

    public void setHotel(ArrayList<HashMap<String, Object>> hotel) {
        Hotel = hotel;
    }

    public ArrayList<HashMap<String, Object>> getRestaurants() {
        return Restaurants;
    }

    public void setRestaurants(ArrayList<HashMap<String, Object>> restaurants) {
        Restaurants = restaurants;
    }

    public ArrayList<Object> getPlacesVisited() {
        return PlacesVisited;
    }

    public void setPlacesVisited(ArrayList<Object> placesVisited) {
        PlacesVisited = placesVisited;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPrimaryID() {
        return primaryID;
    }

    public void setPrimaryID(String primaryID) {
        this.primaryID = primaryID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }


}
