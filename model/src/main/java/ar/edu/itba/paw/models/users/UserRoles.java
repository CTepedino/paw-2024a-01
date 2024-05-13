package ar.edu.itba.paw.models.users;

public enum UserRoles {
    READER("READER"),
    WRITER("WRITER");

    private final String role;
    UserRoles(String role){
        this.role = role;
    }

    @Override
    public String toString(){
        return role;
    }
}
