package com.customer.account.request;

import com.customer.account.validator.CountryCode;
import com.customer.account.validator.DateFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "Username is mandatory")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;

    @NotBlank(message = "First name is mandatory")
    @Size(max = 20, message = "First name has maximum 20 of characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(max = 20, message = "First name has maximum 20 of characters")
    private String lastName;

    @Size(max = 20, message = "First name has maximum 20 of characters")
    @NotBlank(message = "Street is mandatory")
    private String street;

    @Size(max = 20, message = "First name has maximum 20 of characters")
    @NotBlank(message = "City is mandatory")
    private String city;

    @Size(max = 20, message = "First name has maximum 20 of characters")
    @NotBlank(message = "Region is mandatory")
    private String region;

    @Size(max = 6, message = "Postcode has maximum of 6 characters")
    @NotBlank(message = "Postal code is mandatory")
    private String postalCode;

    @Size(max = 2, message = "Country code has maximum of 2 characters")
    @NotBlank(message = "Country code is mandatory")
    @CountryCode(message = "Country code must be a valid ISO 3166-1 alpha-2 code")
    private String countryCode;

    @Size(max = 10, message = "First name has maximum 10 of characters")
    @NotBlank(message = "Document ID is mandatory")
    private String documentId;

    @NotBlank(message = "Date of birth is mandatory")
    @DateFormat(pattern = "ddMMyyyy")
    private String dateOfBirth;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
