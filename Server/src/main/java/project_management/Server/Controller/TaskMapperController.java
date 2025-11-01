package project_management.Server.Controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import project_management.Server.DTO.TaskMapperDto;
import project_management.Server.Model.EmployeeModel;
import project_management.Server.Model.TaskModel;
import project_management.Server.Model.TaskRequestModel;
import project_management.Server.Repository.EmployeeRepository;
import project_management.Server.Repository.TaskRepository;

@RestController
public class TaskMapperController {
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TaskRepository taskRepository;

    ObjectMapper objectMapper=new ObjectMapper();

    RestClient restClient = RestClient.create();


    String prompt ="Here I have the project description ,tasks and list of team members . Can you map the tasks to members such that every members must get one task which is closest to their skills . If they have similar skills and there will be tie situation on some task then consider other factors such as experience , certificates etc . The answer should be in key value pair . where the key should be memployee id and value should be taskid . Just return the key value pair and no other texts eg : {1 : 4d827754-5975-48c8-8e16-945b7a32bba8 , 2 : 4d8ags7754-5975-48c8-8e16-945b7a32bba8 , 3: 4d827754-59agha25-48c8-8e16-945b7a32bba8}";
    @PostMapping("maptasks")
    public Map<Integer,String> taskMapper(@RequestBody TaskMapperDto taskMapperDto){
        try{
        
        List<String> taskId=taskMapperDto.getTaskId();
        List<Integer> empId= taskMapperDto.getEmpId();
        
        ArrayList<TaskModel> tasks=new ArrayList<>();
        
        for(String i: taskId){
            TaskModel task = taskRepository.findByTaskId(i);
            tasks.add(task);
        }

        HashMap<Integer,String> user= new HashMap<>();

        for(Integer i: empId){
            EmployeeModel emp = employeeRepository.findByEmployeeId(i);
            String bio=emp.getEmployeeBio();
            user.put(i, bio);
        }

            String taskJson = objectMapper.writeValueAsString(tasks);
            String membersJson = objectMapper.writeValueAsString(user);

            String finalInput = taskJson + " " +membersJson + " , " + prompt;
            // System.out.println(finalInput);

            Map<String, Object> body = Map.of(
                "contents", new Object[]{
                    Map.of("parts", new Object[]{
                        Map.of("text",finalInput)
                    })
                }
            );

            ResponseEntity<String> response = restClient.post()
                .uri(geminiApiUrl+geminiApiKey)
                .header("Content-Type","application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toEntity(String.class);

            String jsonResponse = response.getBody();
            JsonNode root = objectMapper.readTree(jsonResponse);

            String result = root.path("candidates")
                            .get(0)
                            .path("content")
                            .path("parts")
                            .get(0)
                            .path("text")
                            .asText();

            result.trim();
            String res =result.substring(7,result.length()-4);
            System.out.println(res);
            Map<Integer,String> emailTaskMap = objectMapper.readValue(res, new TypeReference<Map<Integer,String>>() {});


            emailTaskMap.forEach((id,taskid) -> {
                EmployeeModel emp = employeeRepository.findByEmployeeId(id);
                TaskModel taskModel = taskRepository.findByTaskId(taskid);
                taskModel.setEmployeeModel(emp);
                taskRepository.save(taskModel);
            });

            return emailTaskMap;
        }catch(Exception e){
            throw new RuntimeException("Failed to parse JSON: " + e.getMessage());
        }
    }
}
