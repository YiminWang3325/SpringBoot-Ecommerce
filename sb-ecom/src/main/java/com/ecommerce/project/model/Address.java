package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name is at least 5")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name is at least 5")
    private String buildingName;

    @NotBlank
    @Size(min = 5, message = "City name is at least 5")
    private String city;

    @NotBlank
    @Size(min = 5, message = "Country name is at least 5")
    private String country;

    @NotBlank
    @Size(min = 5, message = "State name is at least 5")
    private String state;

    @NotBlank
    @Size(min = 5, message = "Pincode name is at least 5")
    private String pincode;

    public Address(String pincode, String state, String country, String city, String buildingName, String street) {
        this.pincode = pincode;
        this.state = state;
        this.country = country;
        this.city = city;
        this.buildingName = buildingName;
        this.street = street;
    }
    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();


}
