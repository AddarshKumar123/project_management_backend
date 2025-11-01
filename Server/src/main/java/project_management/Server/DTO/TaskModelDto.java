package project_management.Server.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskModelDto {
    String taskId;
    String taskTitle;
    String description;
    String status;
    String priority;
    String dueDate;
    String createdDate;
    private EmployeeDto employee;
}
