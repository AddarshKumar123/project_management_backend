package project_management.Server.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project_management.Server.Model.TaskModel;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    String firstName;
    String email;
    Integer employeeId;
}
