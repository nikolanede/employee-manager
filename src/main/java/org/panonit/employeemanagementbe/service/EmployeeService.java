package org.panonit.employeemanagementbe.service;

import org.panonit.employeemanagementbe.domain.Employee;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private static final String FILE_PATH = "employees.txt";

    public EmployeeService() {
        initializeFile();
    }

    private void initializeFile() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error initializing file", e);
        }
    }

    public Mono<Void> addEmployee(Employee employee) {
        return Mono.fromRunnable(() -> {
            try {
                Files.write(Paths.get(FILE_PATH),
                        (employee.toString() + "\n").getBytes(),
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException("Error writing to file", e);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    public Flux<Employee> getAllEmployees() {
        return Mono.fromCallable(() -> Files.readAllLines(Paths.get(FILE_PATH)))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .flatMap(line -> Mono.justOrEmpty(Employee.fromString(line))); // FIX HERE
    }

    public Mono<Employee> getEmployeeByName(String name) {
        return getAllEmployees()
                .filter(employee -> employee.getName().equalsIgnoreCase(name))
                .next();
    }

    public Mono<Void> updateEmployee(String name, Employee updatedEmployee) {
        return getAllEmployees()
                .collectList()
                .flatMap(list -> {
                    List<String> updatedList = list.stream()
                            .map(e -> e.getName().equalsIgnoreCase(name) ? updatedEmployee : e)
                            .map(Employee::toString)
                            .collect(Collectors.toList());
                    return writeToFile(updatedList);
                });
    }

    public Mono<Void> deleteEmployee(String name) {
        return getAllEmployees()
                .filter(employee -> !employee.getName().equalsIgnoreCase(name))
                .collectList()
                .flatMap(list -> writeToFile(
                        list.stream().map(Employee::toString).toList()));
    }

    private Mono<Void> writeToFile(List<String> lines) {
        return Mono.fromRunnable(() -> {
            try {
                Files.write(Paths.get(FILE_PATH), lines, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Error writing to file", e);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}
