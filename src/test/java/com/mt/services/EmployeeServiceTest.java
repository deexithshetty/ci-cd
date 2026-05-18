package com.mt.services;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class EmployeeServiceTest {

    @Test
    public void testGetEmployeeDetails() throws Exception {

        EmployeeService service = new EmployeeService();

        String response = service.uploadImage(null, null, null);

        assertTrue(response.contains("Mithun Technologies"));

    }
}
