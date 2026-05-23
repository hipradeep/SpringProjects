package com.hipradeep.code.controller;

import com.hipradeep.code.dto.EmployeeRequest;
import com.hipradeep.code.entity.Employee;
import com.hipradeep.code.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeRequest request) {
        Employee savedEmployee = employeeService.saveEmployee(request);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }
}
