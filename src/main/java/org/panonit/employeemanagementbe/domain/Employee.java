package org.panonit.employeemanagementbe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private String name;
    private String surname;
    private int age;
    private String email;
    private String gender;

    @Override
    public String toString() {
        return name + "," + surname + "," + age + "," + email + "," + gender;
    }

    public static Employee fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 5) return null;
        return new Employee(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4]);
    }
}
