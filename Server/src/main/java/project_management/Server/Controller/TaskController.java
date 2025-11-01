package project_management.Server.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import project_management.Server.DTO.EmployeeDto;
import project_management.Server.DTO.TaskDto;
import project_management.Server.DTO.TaskModelDto;
import project_management.Server.Model.EmployeeModel;
import project_management.Server.Model.ProjectModel;
import project_management.Server.Model.TaskModel;
import project_management.Server.Model.TaskRequestModel;
import project_management.Server.Repository.EmployeeRepository;
import project_management.Server.Repository.ProjectRepository;
import project_management.Server.Repository.TaskRepository;

@RestController
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;
   
    @PostMapping("createtask/{projectId}")
    public String createTask(@PathVariable Integer projectId,@RequestBody TaskModel taskModel){
        ProjectModel project = projectRepository.findByProjectId(projectId);
        taskModel.setTaskId(UUID.randomUUID().toString());
        project.getTasks().add(taskModel);
        projectRepository.save(project);
        return "Task created";
    }

    @GetMapping("gettasks/{project_id}")
    // @Cacheable(value = "task" ,key = "#project_id")
    public List<TaskModelDto> getTasks(@PathVariable("project_id") Integer project_id){
        ProjectModel project=projectRepository.findByProjectId(project_id);
        List<TaskModelDto> task= project.getTasks().
        stream().map(tasks ->{
            TaskModelDto taskModelDto = new TaskModelDto();
                    taskModelDto.setDescription(tasks.getDescription());
                    taskModelDto.setTaskId(tasks.getTaskId());
                    taskModelDto.setStatus(tasks.getStatus());
                    taskModelDto.setTaskTitle(tasks.getTaskTitle());
                    taskModelDto.setPriority(tasks.getPriority());
                    taskModelDto.setDueDate(tasks.getDueDate());
                    taskModelDto.setCreatedDate(tasks.getCreatedDate());
            
            if(tasks.getEmployeeModel()!=null){
                EmployeeDto employeeDto = modelMapper.map(tasks.getEmployeeModel(), EmployeeDto.class);
                taskModelDto.setEmployee(employeeDto);
            }

            return taskModelDto;
        })
        .collect(Collectors.toList());
        // return new ResponseEntity<>(task,HttpStatus.OK);
        return task;
    }

    @GetMapping("gettaskbyid/{taskid}")
    // @Cacheable(value = "task" ,key = "#taskId")
    public TaskModelDto getTaskById(@PathVariable("taskid") String taskId){
        TaskModel task=taskRepository.findByTaskId(taskId);
        TaskModelDto taskModelDto=new TaskModelDto();

        taskModelDto.setDescription(task.getDescription());
        taskModelDto.setTaskId(task.getTaskId());
        taskModelDto.setStatus(task.getStatus());
        taskModelDto.setTaskTitle(task.getTaskTitle());
        taskModelDto.setPriority(task.getPriority());
        taskModelDto.setDueDate(task.getDueDate());
        taskModelDto.setCreatedDate(task.getCreatedDate());

        if(task.getEmployeeModel()!=null){
            EmployeeDto employeeDto = modelMapper.map(task.getEmployeeModel(), EmployeeDto.class);
            taskModelDto.setEmployee(employeeDto);
        }

        // return new ResponseEntity<>(taskModelDto,HttpStatus.OK);
        return taskModelDto;
    }

    @PutMapping("updateTask/{taskid}")
    public String updateTaskStatus(@PathVariable("taskid") String taskId,@RequestBody TaskDto updatedTask){
        TaskModel task=taskRepository.findByTaskId(taskId);
        task.setTaskTitle(updatedTask.getTaskTitle());
        task.setDescription((updatedTask.getDescription()));
        task.setStatus(updatedTask.getStatus());
        task.setPriority(updatedTask.getPriority());
        task.setDueDate(updatedTask.getDueDate());
        task.setCreatedDate(updatedTask.getCreatedDate());
        taskRepository.save(task);
        return "Status Updated";
    }


}
