package project_management.Server.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    Integer projectId;
    String projectName;
    String projectDesc;
    String status;
    String priority;
    String dueDate;
    List<String>teamMembers;
}
