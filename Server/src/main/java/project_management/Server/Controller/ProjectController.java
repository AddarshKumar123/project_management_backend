package project_management.Server.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import project_management.Server.DTO.ProjectDto;
import project_management.Server.Model.ProjectModel;
import project_management.Server.Service.ProjectService;

@RestController
public class ProjectController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProjectService projectService; 

    @PostMapping("/addproject")
    public ResponseEntity<ProjectModel>addProject(@RequestBody ProjectModel projectModel){  
        return projectService.addProject(projectModel);
    }

    @GetMapping("/getproject")
    public List<ProjectDto> getProject(){
        return projectService.getProject();
    }

    @GetMapping("/getProjectById/{projectId}")
    public ProjectDto getProjectById(@PathVariable Integer projectId){
        return projectService.getProjectById(projectId);
    }

    @PutMapping("updateProject/{projectId}")
    public String updateProjectInfo(@PathVariable Integer projectId,@RequestBody ProjectDto projectDto){
        return projectService.updateProjectInfo(projectId,projectDto);
    }

}
