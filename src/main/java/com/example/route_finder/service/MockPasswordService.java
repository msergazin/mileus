package com.example.route_finder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MockPasswordService {
    private final String PASSWORD = "Mileus";
    @Autowired
    public MockPasswordService(){ }

    public Boolean checkPass(String pass) {
        System.out.println("pass: " + pass);
        return !pass.equals(PASSWORD);
    }

}
