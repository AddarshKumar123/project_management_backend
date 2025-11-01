package project_management.Server.Model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project")
public class ProjectModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer projectId;

    String projectName;
    String projectDesc;
    String status;
    String priority;
    String dueDate;
    List<String>teamMembers;
    


    @OneToMany(
        cascade = CascadeType.ALL
    )
    @JoinColumn(name= "project_id", referencedColumnName = "projectId")
    List<TaskModel>tasks = new ArrayList<>();

}
