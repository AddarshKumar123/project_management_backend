package project_management.Server.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project_management.Server.Model.TaskModel;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    String taskId;
    String taskTitle;
    String description;
    String status;
    String priority;
    String dueDate;
    String createdDate;
}
