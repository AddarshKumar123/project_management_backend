package project_management.Server.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.api.Http;
import org.springframework.http.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import project_management.Server.DTO.EmployeeDto;
import project_management.Server.DTO.LoginDto;
import project_management.Server.Model.EmployeeModel;
import project_management.Server.Repository.EmployeeRepository;
import project_management.Server.UserRoles;
import project_management.Server.Util.JwtUtility;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtility jwtUtility;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    RestClient restClient = RestClient.create();

    ObjectMapper objectMapper=new ObjectMapper();

    String prompt = "Generate a short professional bio in plain English using the given details: skills, years of experience, and certifications. The bio should be concise, natural, and avoid code or bullet points. Example format: 'John is a software engineer with 3 years of experience, skilled in Java and Spring Boot.' make sure to include all factors which are mentioned if provided .";

    public ResponseEntity<String> register(EmployeeModel employeeModel){
        try{
            String email=employeeModel.getEmail();
            String password=employeeModel.getEncrypted_password();

            if(employeeRepository.findByEmail(email) != null){
                return new ResponseEntity<>("User already exist with this email",HttpStatus.OK);
            }

            String finalInput=prompt+ " "+ " This is the string : " + "name : " + employeeModel.getFirstName() + " YearsOfExperience : " + employeeModel.getYearsOfExperience() + " , Skills : " + employeeModel.getSkills();

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

            employeeModel.setEmployeeBio(result);
            employeeModel.setRole(UserRoles.USER);
            employeeModel.setEncrypted_password(passwordEncoder.encode(password));
            employeeRepository.save(employeeModel);

            String token = jwtUtility.generateToken(email,employeeModel.getRole().name());
            return new ResponseEntity<>(token,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> AuthenticateEmployee(LoginDto loginDto){
        try{
            String email=loginDto.getEmail();
            String encrypted_password=loginDto.getEncrypted_password();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,encrypted_password));

            EmployeeModel employeeModel= employeeRepository.findByEmail(email);
            System.out.println(employeeModel.getRole().name());
            String token=jwtUtility.generateToken(employeeModel.getEmail(),employeeModel.getRole().name());

            ResponseCookie jwtCookie=ResponseCookie.from("jwt",token)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(24*60*60)
                    .sameSite("Lax")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString()).body("Logged in Successfully");
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<List<EmployeeDto>> getEmployee(){
        List<EmployeeDto> emp = employeeRepository.findAll()
                .stream().map(employee -> modelMapper.map(employee,EmployeeDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(emp,HttpStatus.OK);
    }
}
