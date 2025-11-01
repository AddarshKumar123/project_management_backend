package project_management.Server.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import project_management.Server.Model.EmployeeModel;
import project_management.Server.Repository.EmployeeRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        EmployeeModel user=userRepository.findByEmail(email);

        return User.builder()
                .username(user.getEmail())
                .password(user.getEncrypted_password())
                .build();
    }

}
