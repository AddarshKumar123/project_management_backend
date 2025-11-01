package project_management.Server.Model;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestModel {

    ArrayList<TaskModel> tasks = new ArrayList<>();
    ArrayList<EmployeeModel> teamMembers = new ArrayList<>();
}
