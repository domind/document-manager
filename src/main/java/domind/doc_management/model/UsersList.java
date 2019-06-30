package domind.doc_management.model;

import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "user")
public class UsersList {

    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String userName;
    private String password;
    private String token;


    public Long getId() {
        return id;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }

    public String getToken(){
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

}
