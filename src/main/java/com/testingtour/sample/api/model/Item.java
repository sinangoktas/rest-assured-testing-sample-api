package com.testingtour.sample.api.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Item")
@Data
public class Item {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String type;

    @Column
    private float price;

    @Column
    private int count;


}
