package domind.doc_management.web;

import domind.doc_management.model.UsersList;
import domind.doc_management.model.UsersRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UsersController {

    private UsersRepository usersRepository;

    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping("/user")
    ResponseEntity<?> getUsersList(@RequestParam("user") String user, @RequestParam("password") String password) {
        UsersList usersList = usersRepository.findByUserName(user);
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString()+"|"+Integer.toString((int) (System.currentTimeMillis()/1000));
        if (usersList == null)
            return new ResponseEntity<String>("User name does not exist, or wrong password", HttpStatus.BAD_REQUEST);
        else {
            if (usersList.getPassword().equals(password)) {
                usersList.setToken(randomUUIDString);
                usersRepository.save(usersList);
                ResponseEntity<String> x = new ResponseEntity<String>(
                        "{\"user\":\"" + user + "\",\"token\":\"" + randomUUIDString + "\" }", HttpStatus.OK);
                return x;

            } else
                return new ResponseEntity<String>("User name does not exist, or wrong password", HttpStatus.BAD_REQUEST);
        }

    }

}