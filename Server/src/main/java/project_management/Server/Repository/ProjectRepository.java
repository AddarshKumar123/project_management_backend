package project_management.Server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project_management.Server.Model.ProjectModel;

public interface ProjectRepository extends JpaRepository<ProjectModel,Integer> {
    ProjectModel findByProjectId(Integer projectId);
}
