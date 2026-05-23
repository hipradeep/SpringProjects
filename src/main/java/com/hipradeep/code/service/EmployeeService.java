package com.hipradeep.code.service;

import com.hipradeep.code.dto.EmployeeRequest;
import com.hipradeep.code.entity.Department;
import com.hipradeep.code.entity.Employee;
import com.hipradeep.code.repository.DepartmentRepository;
import com.hipradeep.code.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public Employee saveEmployee(EmployeeRequest request) {
        // 1. Fetch the department from the database (The Inverse side)
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + request.getDepartmentId()));

        // 2. Create the new Employee
        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());

        // 3. MOST IMPORTANT: Set the Department on the Employee (The Owning side)
        // This is what actually saves the foreign key to the database!
        employee.setDepartment(department);

        // 4. Save the employee
        return employeeRepository.save(employee);
    }
}
