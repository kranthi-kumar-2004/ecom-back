package com.student.management.dto;

public class ProfileResponse {

    private String name;
    private String username; // email stored as username
      public ProfileResponse(String name, String username) {
        this.name = name;
        this.username = username;

    }
  
    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }


}