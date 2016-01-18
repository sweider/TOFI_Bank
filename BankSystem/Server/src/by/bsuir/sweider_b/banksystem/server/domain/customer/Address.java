package by.bsuir.sweider_b.banksystem.server.domain.customer;

import by.bsuir.sweider_b.banksystem.server.domain.activerecord.ActiveRecord;

import javax.persistence.*;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "addresses")
public class Address extends ActiveRecord {

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "building")
    private String building;

    @Column(name = "room")
    private String room;

    private Address() {
    }

    public Address(String city, String street, String building, String room) {
        this.city = city;
        this.street = street;
        this.building = building;
        this.room = room;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
