package com.customer.account.entity;
import jakarta.persistence.*;

/**
 * Address entity assigned to Customer
 */
@Entity
@Table(name = "address")
public class Address {
    /**
     * Unique Identifier for Address
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The Address street, maximum length of 20
     */
    @Column(nullable = false, length = 20)
    private String street;

    /**
     * The Address city, maximum length of 20
     */
    @Column(nullable = false, length = 20)
    private String city;

    /**
     * The Address region, maximum length of 20
     */
    @Column(nullable = false, length = 20)
    private String region;

    /**
     * The Address postcode, maximum length of 6
     */
    @Column(nullable = false, length = 6)
    private String postalCode;

    /**
     * The Address country, limited to NL or BE
     */
    @Column(nullable = false, length = 2)
    private String country;

    public Address() {

    }

    public Address(String street, String city, String region,
                   String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
