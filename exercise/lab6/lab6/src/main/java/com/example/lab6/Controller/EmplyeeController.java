package com.example.lab6.Controller;

import com.example.lab6.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/emplyee")
public class EmplyeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity<?> getEmployees(){
        return ResponseEntity.status(200).body(employees);
    }//get list of all employee


    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody @Valid Employee employee , Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }

        employees.add(employee);
        return ResponseEntity.status(200).body("Employee added successfully ! ");
    }//add employee to the list


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id , @RequestBody @Valid Employee employee , Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }

        for (int i = 0 ; i < employees.size() ; i++) {
            if (employees.get(i).getId().equals(id)) {
                employees.set(i, employee);
                return ResponseEntity.status(200).body("Employee update successfully");
            }
        }
        return ResponseEntity.status(404).body("Employee not found");
    }//update employee


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id){
        for(int i = 0 ; i < employees.size() ; i++){
            if(employees.get(i).getId().equals(id)){
                employees.remove(i);
                return ResponseEntity.status(200).body("Employee removed successfully");
            }
        }
        return ResponseEntity.status(404).body("Employee not found");
    }//remove emplyee

    @GetMapping("/search/{position}")
    public ResponseEntity<?> searchByPosition(@PathVariable String position){
        ArrayList<Employee> positionList = new ArrayList<>() ;

        if(!position.equalsIgnoreCase("supervisor") &&
           !position.equalsIgnoreCase("coordinator")){
            return ResponseEntity.status(400).body("invalid position");
        }

        for(int i = 0 ; i < employees.size() ; i++){
            if(employees.get(i).getPosition().equalsIgnoreCase(position)){
                positionList.add(employees.get(i));
            }
        }
        return ResponseEntity.status(200).body(positionList);
    }//search for employee by position

    @GetMapping("/get-age-range/{minAge}/{maxAge}")
    public ResponseEntity<?> getAgeRange(@PathVariable int minAge , @PathVariable int maxAge){

        ArrayList<Employee> ageList = new ArrayList<>();

        if(minAge < 26 || maxAge < 26){
            return ResponseEntity.status(400).body("Minimum and maximum age should be greater than 25");
        }

        if(minAge > maxAge){
            return ResponseEntity.status(400).body("Minimum Age cannot be greater than maximum Age");
        }

        for(int i = 0 ; i < employees.size() ; i++){
            if(employees.get(i).getAge() >= minAge && employees.get(i).getAge() <= maxAge){
                ageList.add(employees.get(i));
            }
        }

        if(ageList.isEmpty()){
            return ResponseEntity.status(404).body("No employees found");
        }
        return ResponseEntity.status(200).body(ageList);
    }//get employee by age range


    @GetMapping("/annual-leave/{id}")
    public ResponseEntity<?> applyForAnnualLeave(@PathVariable String id){

        for(int i = 0 ; i < employees.size() ; i++){
                Employee currentEmployee = employees.get(i);

        if (currentEmployee.getId().equals(id)) {

        if (currentEmployee.isOnLeave()) {
             return ResponseEntity.status(400).body("Employee already on leave");
             }

        if (currentEmployee.getAnnualLeave() < 1) {
             return ResponseEntity.status(400).body("No annual leave balance");
             }

            currentEmployee.setOnLeave(true);
            currentEmployee.setAnnualLeave(currentEmployee.getAnnualLeave() - 1);

              return ResponseEntity.status(200).body("Leave applied successfully");
            }
            }
            return ResponseEntity.status(404).body("Employee not found");

        }//apply For AnnualLeave


    @GetMapping("/no-leave")
    public ResponseEntity<?> getEmployeesWithNoAnnualLeave() {

        ArrayList<Employee> noAnnualLeaveList = new ArrayList<>();

        for (Employee emp : employees) {
            if (emp.getAnnualLeave() <= 0) {
                noAnnualLeaveList.add(emp);
            }
        }

        if (noAnnualLeaveList.isEmpty()) {
            return ResponseEntity.status(404).body("No employees with zero annual leave");
        }

        return ResponseEntity.status(200).body(noAnnualLeaveList);
    }//get employee with no annual leave


    public ResponseEntity<?> promoteToSupervisor(@PathVariable String employeeId, @PathVariable String requesterId){

        Employee promoter = null ;

        for (Employee emp : employees) {
            if (emp.getId().equals(requesterId)) {
                promoter = emp;
                break;
            }
        }

        if (promoter == null) {
            return ResponseEntity.status(404).body("Promoter not found");
        }

        if (!promoter.getPosition().equalsIgnoreCase("supervisor")) {
            return ResponseEntity.status(403).body("Only supervisors can promote employees");
        }

        for (Employee emp : employees) {
            if (emp.getId().equals(employeeId)) {
                if (emp.isOnLeave()) {
                    return ResponseEntity.status(400).body("Employee is on leave");
                }

                if (emp.getAge() < 30) {
                    return ResponseEntity.status(400).body("Employee must be at least 30 years old");
                }

                emp.setPosition("supervisor");
                return ResponseEntity.status(200).body("Employee promoted successfully");
            }
        }
        return ResponseEntity.status(404).body("Employee not found");
    }//promote the employee

    }


