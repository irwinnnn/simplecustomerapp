package com.customer.account.service.impl;

import com.customer.account.entity.Address;
import com.customer.account.entity.Customer;
import com.customer.account.entity.IbanAccount;
import com.customer.account.repository.CustomerRepository;
import com.customer.account.request.RegisterRequest;
import com.customer.account.response.dto.CustomerAccountOverviewDTO;
import com.customer.account.response.mapper.CustomerAccountOverviewMapper;
import com.customer.account.service.RegisterCustomerService;
import org.apache.commons.lang3.RandomStringUtils;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Objects;

@Service
public class RegisterCustomerServiceImpl implements RegisterCustomerService {
    private enum BankCode {
        NL("NL", CountryCode.NL,"NLBN"),
        BE("BE", CountryCode.BE, "123");

        private final String key;
        private final CountryCode countryCode;
        private final String bankCode;

        BankCode(String key, CountryCode countryCode, String bankCode) {
            this.key = key;
            this.countryCode = countryCode;
            this.bankCode = bankCode;
        }

        public String getKey() {
            return key;
        }

        public CountryCode getCountryCode() {
            return countryCode;
        }

        public String getBankCode() {
            return bankCode;
        }

        public static CountryCode getCountryCode(String key) {
            for (BankCode value : BankCode.values()) {
                if (value.getKey().equals(key)) {
                    return value.getCountryCode();
                }
            }
            throw new IllegalArgumentException("Invalid country key: " + key);
        }

        public static String getBankCode(String key) {
            for (BankCode value : BankCode.values()) {
                if (value.getKey().equals(key)) {
                    return value.getBankCode();
                }
            }
            throw new IllegalArgumentException("Invalid country key: " + key);
        }
    }
    private final CustomerRepository customerRepository;
    private final Pbkdf2PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterCustomerServiceImpl(CustomerRepository customerRepository, Pbkdf2PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String registerCustomer(RegisterRequest registerRequest) {
        final String password = RandomStringUtils.
                random(10, 1, 122, true, true, null, new SecureRandom());
        final Customer customer = new Customer(registerRequest.getUsername(), registerRequest.getFirstName(), registerRequest.getLastName(),
                passwordEncoder.encode(password), registerRequest.getDocumentId(), registerRequest.getDateOfBirth());

        final Address address = new Address(registerRequest.getStreet(), registerRequest.getCity(), registerRequest.getRegion(),
                registerRequest.getPostalCode(), registerRequest.getCountryCode());
        customer.setAddress(address);

        Iban iban = new Iban.Builder()
                .countryCode(BankCode.getCountryCode(registerRequest.getCountryCode()))
                .bankCode(BankCode.getBankCode(registerRequest.getCountryCode()))
                .buildRandom();
        final IbanAccount ibanAccount = new IbanAccount("Savings", new BigDecimal("200"), "EUR", iban.toString());
        customer.setIbanAccount(ibanAccount);

        customerRepository.save(customer);
        return password;
    }

    @Override
    public CustomerAccountOverviewDTO getCustomerAccountOverview(String username) {
        Customer customer = customerRepository.findByUsername(username).orElse(null);
        if (!Objects.isNull(customer)){
            return CustomerAccountOverviewMapper.toCustomerDTO(customer);
        }
        return null;
    }
}
