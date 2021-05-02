package com.example.rest.controller;

import com.example.rest.dao.EmployeeDAO;
import com.example.rest.model.Employee;
import com.example.rest.model.Employees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping
public class EmployeeController
{
    @Autowired
    private EmployeeDAO employeeDao;

    @GetMapping("/")
    public String testUri() {
        return "Welcome !!!!!!";
    }

    @GetMapping(path="/employees", produces = "application/json")
    public Employees getEmployees()
    {
        return employeeDao.getAllEmployees();
    }

    @PostMapping(path= "/employees", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addEmployee(@RequestBody Employee employee)
    {
        Integer id = employeeDao.getAllEmployees().getEmployeeList().size() + 1;
        employee.setId(id);

        employeeDao.addEmployee(employee);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(employee.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(path="/employees/sort", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Employee>> sortEmployees() {
        List<Employee> employeeList = employeeDao.getAllEmployees().getEmployeeList();
        Collections.sort(employeeList, (e1, e2) -> e1.getFirstName().compareTo(e2.getFirstName()));
        return new ResponseEntity(employeeList, HttpStatus.OK);
    }
}
