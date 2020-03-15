package com.example.receipt_app;

@Entity
public class User {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;
}
