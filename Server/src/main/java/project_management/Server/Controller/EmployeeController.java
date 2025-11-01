package project_management.Server.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import project_management.Server.DTO.EmployeeDto;
import project_management.Server.DTO.LoginDto;
import project_management.Server.Model.EmployeeModel;
import project_management.Server.Service.EmployeeService;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/createEmployee")
    public ResponseEntity<String> createEmployee(@RequestBody EmployeeModel employeeModel) {
        try{
            return employeeService.register(employeeModel);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> AuthenticateEmployee(@RequestBody LoginDto loginDto){
        try{
            return employeeService.AuthenticateEmployee(loginDto);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getemployee")
    public ResponseEntity<List<EmployeeDto>> getEmployee(){
        return employeeService.getEmployee();
    }
}
