package project_management.Server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project_management.Server.Model.EmployeeModel;

public interface EmployeeRepository extends JpaRepository<EmployeeModel,Integer> {
    EmployeeModel findByEmail(String email);
    EmployeeModel findByEmployeeId(Integer employeeId);
}
