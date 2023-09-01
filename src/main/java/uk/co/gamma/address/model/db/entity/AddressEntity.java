package uk.co.gamma.address.model.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Address")
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String building;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private String town;
    @Column(nullable = false)
    private String postcode;

    public AddressEntity(Integer id, String building, String street, String town, String postcode) {
        this.id = id;
        this.building = building;
        this.street = street;
        this.town = town;
        this.postcode = postcode;
    }

    public AddressEntity(String building, String street, String town, String postcode) {
        this.building = building;
        this.street = street;
        this.town = town;
        this.postcode = postcode;
    }

    public AddressEntity() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
