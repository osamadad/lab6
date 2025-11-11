package com.tuwaiq.lab6_employeemanagmentsystem.Controller;

import Api.ApiResponse;
import com.tuwaiq.lab6_employeemanagmentsystem.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee-management-system")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity<?> getEmployee() {
        if (this.employees.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("There are no employees to show"));
        } else {
            return ResponseEntity.status(200).body(this.employees);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        } else {
            this.employees.add(employee);
            return ResponseEntity.status(200).body(new ApiResponse("The employee have been added successfully"));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        } else {
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getId().equalsIgnoreCase(id)) {
                    employees.set(i, employee);
                    return ResponseEntity.status(200).body(new ApiResponse("The employee have been updated successfully"));
                }
            }
            return ResponseEntity.status(400).body(new ApiResponse("No employee with that id found, please try again"));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id) {
        for (Employee employee : employees) {
            if (employee.getId().equalsIgnoreCase(id)) {
                employees.remove(employee);
                return ResponseEntity.status(200).body(new ApiResponse("The employee have been deleted successfully"));
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("No employee with that id found, please try again"));
    }

    @GetMapping("/get/by-position/{position}")
    public ResponseEntity<?> getEmployeeByPosition(@PathVariable String position) {
        if (position.equalsIgnoreCase("Supervisor")                         /* varify that position is valid */
                || position.equalsIgnoreCase("Coordinator")) {
            ArrayList<Employee> employeesByPosition = new ArrayList<>();
            for (Employee employee : employees) {
                if (employee.getPosition().equalsIgnoreCase(position)) {
                    employeesByPosition.add(employee);
                }
            }
            return ResponseEntity.status(200).body(employeesByPosition);                /* return array */
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Sorry, your Position can only be " +
                    "supervisor or coordinator, please try again."));
        }
    }

    @GetMapping("/get/by-age-range/{minAge}/{maxAge}")
    public ResponseEntity<?> getEmployeeByAgeRange(@PathVariable int minAge, @PathVariable int maxAge) {
        if (minAge < 25 || maxAge < 25) {
            return ResponseEntity.status(400).body(new ApiResponse("Sorry, employee " +             /* varify age */
                    "minimum age can't be less than 25 years old, please try again."));
        } else {
            ArrayList<Employee> employeesByAge = new ArrayList<>();
            for (Employee employee : this.employees) {
                if (employee.getAge() >= minAge) {
                    if (employee.getAge() <= maxAge) {
                        employeesByAge.add(employee);
                    }
                }
            }
            if (employeesByAge.isEmpty()) {
                return ResponseEntity.status(400).body(new ApiResponse("No employees of this range group, please try again"));
            }
            return ResponseEntity.status(200).body(employeesByAge);                 /* return array if employees found */
        }
    }

    @PutMapping("/apply/annual-leave/{id}")
    public ResponseEntity<?> applyForAnnualLeave(@PathVariable String id) {
        for (Employee employee : employees) {
            if (employee.getId().equalsIgnoreCase(id)) {                     /* check if valid id */
                if (!employee.getOnLeave()) {                                /* check if employee is already on leave */
                    if (employee.getAnnualLeave() > 0) {                       /* enough leave days reserve */
                        employee.setOnLeave(true);
                        employee.setAnnualLeave(employee.getAnnualLeave() - 1);
                        return ResponseEntity.status(200).body(new ApiResponse("You have successfully apply" +
                                " for an annual leave, your annual leave reserve is: " + employee.getAnnualLeave()));
                    } else {
                        return ResponseEntity.status(400).body(new ApiResponse("You don't have enough reserve leave days"));
                    }
                } else {
                    return ResponseEntity.status(400).body(new ApiResponse("You are already on annual leave, apply again after you come back"));
                }
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("No employee with that id found, please try again"));
    }

    @GetMapping("/get/no-annual-leave")
    public ResponseEntity<?> getEmployeeWithNoAnnualLeave() {
        ArrayList<Employee> employeesWithNoAnnualLeaves = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getAnnualLeave() == 0) {
                employeesWithNoAnnualLeaves.add(employee);
            }
        }
        if (employeesWithNoAnnualLeaves.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("There are no employees that have used all their annual leaves"));
        } else {
            return ResponseEntity.status(200).body(employeesWithNoAnnualLeaves);            /* return array */
        }
    }

    @PutMapping("/promote-employee/{supervisorId}/{employeeId}")
    public ResponseEntity<?> promoteEmployee(@PathVariable String supervisorId, @PathVariable String employeeId) {
        Employee supervisorId2 = null;
        Employee employeeId2 = null;
        for (Employee employee : employees) {
            if (employee.getId().equalsIgnoreCase(supervisorId)) {
                if (employee.getPosition().equalsIgnoreCase("Supervisor")) {     /* varify if supervisor */
                    supervisorId2 = employee;
                } else {
                    return ResponseEntity.status(400).body(new ApiResponse("You don't have the access to promote employees"));
                }
            } else if (employee.getId().equalsIgnoreCase(employeeId)) {                       /* check employee id is valid */
                employeeId2 = employee;
            }
        }
        if (supervisorId2 == null) {
            return ResponseEntity.status(400).body(new ApiResponse("No supervisor employee with that id found, please try again"));
        }
        if (employeeId2 == null) {
            return ResponseEntity.status(400).body(new ApiResponse("No employee with that id found, please try again"));
        }
        if (employeeId2.getAge() >= 30) {                                    /* varify age is bigger than 30 */
            if (!employeeId2.getOnLeave()) {                               /* varify employee is not currently on leave */
                employeeId2.setPosition("Supervisor");
                return ResponseEntity.status(200).body(new ApiResponse("The employee with id:" +
                        employeeId + " have been promoted successfully"));
            } else {
                return ResponseEntity.status(400).body(new ApiResponse("The employee is" +
                        " currently on annual leave, please wait for his return"));
            }
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("You don't meet" +
                    " the age requirement to promote, you must be at least 30 years old"));
        }
    }
}
