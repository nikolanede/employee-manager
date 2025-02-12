package org.panonit.employeemanagementbe.controller;

import org.panonit.employeemanagementbe.domain.Employee;
import org.panonit.employeemanagementbe.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public Mono<Void> createEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @GetMapping
    public Flux<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{name}")
    public Mono<Employee> getEmployeeByName(@PathVariable String name) {
        return employeeService.getEmployeeByName(name);
    }

    @PutMapping("/{name}")
    public Mono<Void> updateEmployee(@PathVariable String name, @RequestBody Employee employee) {
        return employeeService.updateEmployee(name, employee);
    }

    @DeleteMapping("/{name}")
    public Mono<Void> deleteEmployee(@PathVariable String name) {
        return employeeService.deleteEmployee(name);
    }
}
