package com.example.CustomerDetails.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Column;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Customers")

public class Customer {
    @Id
    @GeneratedValue
    private int id;
    @NotBlank(message = "Customer name is required")
    private String name;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "phone no should be in 10 digit ")

    private String phoneNumber;
    @NotBlank(message = "address1 is required")
    private String address1;
    @NotBlank(message = "address2 is required")
    private String address2;
    @NotBlank(message = "Email ID is required")
    @Email(message = "Invalid email format")
    private String emailID;
    @NotBlank(message = "PAN card number is required")
    @Column(unique = true)
    private String panCard;
    @NotBlank(message = "Aadhaar card number is required")
    @Column(unique = true)
    private String aadhar;
    @NotNull(message = "Date of birth is required")
    @Past(message = "Invalid date of birth")
    private Date dob;
    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Invalid gender")
    private String gender;

}

