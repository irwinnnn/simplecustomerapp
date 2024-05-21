package com.customer.account.entity;
import jakarta.persistence.*;

/**
 * The Customer entity, primary entity that is created upon registering
 * It has an address and Iban account
 */
@Entity
@Table(name = "customer")
public class Customer {

    /**
     * The unique identifier of Customer
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The Customer username that is used for login, maximum length of 20
     */
    @Column(nullable = false, length = 20)
    private String username;

    /**
     * The Customer first name, maximum length of 20
     */
    @Column(nullable = false, length = 20)
    private String firstName;

    /**
     * The Customer last name, maximum length of 20
     */
    @Column(nullable = false, length = 20)
    private String lastName;

    /**
     * The Customer password that is auto generated
     */
    @Column(nullable = false)
    private String password;

    /**
     * The Customer document id, maximum length of 10
     */
    @Column(nullable = false, length = 10)
    private String documentId;

    /**
     * The customer birthday
     */
    @Column(nullable = false)
    private String dateOfBirth;

    /**
     * The customer ibanAccount, one to one relationship
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ibanAccount", referencedColumnName = "id")
    private IbanAccount ibanAccount;

    /**
     * The customer address, one to one relationship
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address", referencedColumnName = "id")
    private Address address;

    public Customer() {

    }

    public Customer(String username, String firstName, String lastName, String password, String documentId, String dateOfBirth) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.documentId = documentId;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public IbanAccount getIbanAccount() {
        return ibanAccount;
    }

    public void setIbanAccount(IbanAccount ibanAccount) {
        this.ibanAccount = ibanAccount;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
