package com.student.management.enitity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double price;

    private String image;

    private String description;

    private String category;

    private int stock;

    private Double discount_percent;
private Double discount_price;
}