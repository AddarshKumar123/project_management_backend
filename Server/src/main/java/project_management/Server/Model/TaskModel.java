package project_management.Server.Model;

import java.util.Optional;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project_management.Server.DTO.EmployeeDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class TaskModel {

    @Id
    String taskId;
    String taskTitle;
    String description;
    String status;
    String priority;
    String dueDate;
    String createdDate;


    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeModel employeeModel;

    // @ManyToOne
    // @JoinColumn(name= "project_id")
    // private ProjectModel projectModel;

}
