package project_management.Server.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskMapperDto {
    List<String> taskId = new ArrayList<>();
    List<Integer> empId = new ArrayList<>();
}
