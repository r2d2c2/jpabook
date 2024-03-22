package hello.jpabook.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Getter;

@Embeddable@Getter
public class Address {
    private String sity;
    private String street;
    private String zipcode;

    protected Address() {
    }

    public Address(String sity, String street, String zipcode) {
        this.sity = sity;
        this.street = street;
        this.zipcode = zipcode;
    }
}
