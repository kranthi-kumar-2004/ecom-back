package com.student.management.enitity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;
    private String phone;
    private String landmark;
    private String area;
    private String city;
    private String state;
    private String pincode;
}