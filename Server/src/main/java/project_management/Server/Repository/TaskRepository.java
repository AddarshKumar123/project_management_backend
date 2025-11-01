package project_management.Server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project_management.Server.Model.TaskModel;

public interface TaskRepository extends JpaRepository<TaskModel,String> {
    TaskModel findByTaskId(String taskId);
}
