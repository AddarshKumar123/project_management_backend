package project_management.Server;

public enum UserRoles {
    ADMIN("admin"),
    USER("user");

    private String role;

    UserRoles(String role){
        this.role=role;
    }

    public String getValue(){
        return role;
    }
}
