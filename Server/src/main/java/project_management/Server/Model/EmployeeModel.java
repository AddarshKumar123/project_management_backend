package project_management.Server.Model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project_management.Server.UserRoles;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="employee")
public class EmployeeModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    Integer employeeId;

    @Column(name="first_name")
    String firstName;

    @Column(name="last_name")
    String lastName;

    @Column(name="email")
    String email;

    @Column(name="encrypted_password")
    String encrypted_password;

    @Column(name="company")
    String company;

    @Column(name="job_role")
    String job_role;

    @Column(name="employee_bio")
    String employeeBio;

    @OneToMany(
        mappedBy = "employeeModel",
        cascade = CascadeType.ALL
    )
    // @JoinColumn(name= "employee_id", referencedColumnName = "employee_id")
    private List<TaskModel> tasks = new ArrayList<>();

    @Transient
    ArrayList<String> skills=new ArrayList<>();

    @Transient
    String yearsOfExperience;

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private UserRoles role;

    EmployeeModel(ArrayList<String> list,String yearsOfExperience,String firstName,String lastName,String email,String encrypted_password,String company,String job_role){
        for(String i:list){
            this.skills.add(i);
        }

        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.encrypted_password=encrypted_password;
        this.company=company;
        this.job_role=job_role;
        this.yearsOfExperience=yearsOfExperience;
    }
    
}
