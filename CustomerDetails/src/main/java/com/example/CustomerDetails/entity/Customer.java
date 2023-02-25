package com.example.CustomerDetails.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
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
    private String name;
    private Long phoneNumber;
    private String address1;
    private String address2;
    private String emailID;
    private String panCard;
    private String aadhar;
    private Date dob;
    private String gender;

}

