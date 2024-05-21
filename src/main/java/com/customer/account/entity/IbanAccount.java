package com.customer.account.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * IBAN account created for the customer
 */
@Entity
@Table(name = "ibanaccount")
public class IbanAccount {
    /**
     * Unique Identifier for the ibanaccount
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The iban that is generated automatically
     */
    @Column
    private String iban;

    /**
     * The iban account type, default is "Savings"
     */
    @Column(nullable = false)
    private String accountType;

    /**
     * The iban balance
     */
    @Column(nullable = false)
    private BigDecimal balance;

    /**
     * The currency that has EUR by default
     */
    @Column(nullable = false)
    private String currency;

    public IbanAccount() {

    }

    public IbanAccount(String accountType,
                       BigDecimal balance, String currency, String iban) {
        this.accountType = accountType;
        this.balance = balance;
        this.currency = currency;
        this.iban = iban;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
