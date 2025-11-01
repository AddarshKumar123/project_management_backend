package project_management.Server.Service;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import project_management.Server.DTO.ProjectDto;
import project_management.Server.Model.ProjectModel;
import project_management.Server.Repository.ProjectRepository;

@Service
public class ProjectService {


    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ModelMapper modelMapper;
    
    public ResponseEntity<ProjectModel> addProject(ProjectModel projectModel){
        ProjectModel project = projectRepository.save(projectModel);
        return new ResponseEntity<>(project,HttpStatus.OK);
    }

    // @Cacheable(value = "project")
    public List<ProjectDto> getProject(){
        return projectRepository.findAll()
        .stream()
        .map(project -> modelMapper.map(project,ProjectDto.class))
        .collect(Collectors.toList());
    }

    public ProjectDto getProjectById(Integer projectId){
        ProjectModel projectModel=projectRepository.findByProjectId(projectId);
        return modelMapper.map(projectModel,ProjectDto.class);

    }

    public String updateProjectInfo(Integer projectId,ProjectDto projectDto){
        ProjectModel projectModel=projectRepository.findByProjectId(projectId);
        projectModel.setProjectName(projectDto.getProjectName());
        projectModel.setProjectDesc(projectDto.getProjectDesc());
        projectModel.setStatus(projectDto.getStatus());
        projectModel.setPriority(projectDto.getPriority());
        projectModel.setDueDate(projectDto.getDueDate());

        projectRepository.save(projectModel);
        return "Project Info Updated";
    }
}
