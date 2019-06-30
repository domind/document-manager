package domind.doc_management.web;

import domind.doc_management.model.MyTable;
import domind.doc_management.model.MyTableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import domind.doc_management.model.UsersList;
import domind.doc_management.model.UsersRepository;
import domind.doc_management.tool.TokenExpiryCheck;
import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MyTableController {

    private final Logger log = LoggerFactory.getLogger(MyTableController.class);
    private MyTableRepository myTableRepository;

    private UsersRepository usersRepository;
    
    public MyTableController(MyTableRepository myTableRepository, UsersRepository usersRepository) {
        this.myTableRepository = myTableRepository;
        this.usersRepository = usersRepository;
    }
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/myTable")
    Collection<MyTable> myTable() {
        return myTableRepository.findAll();
    }
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/myTable/{id}")
    ResponseEntity<?> getMyTable(@PathVariable Long id) {
        Optional<MyTable> myTable = myTableRepository.findById(id);
        return myTable.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping("/myTable")
    ResponseEntity<?> createMyTable(@Valid @RequestBody MyTable myTable, @RequestHeader String token, @RequestHeader String user) throws URISyntaxException {
        log.info("Request to add myTable: {}", myTable);
        UsersList usersList = usersRepository.findByUserName(user);
        if (usersList.getToken().equals(token) && TokenExpiryCheck.tokenExpiryCheck(token)) {
        MyTable result = myTableRepository.save(myTable);
        return ResponseEntity.created(new URI("/api/myTable/" + result.getId())).body(result);}
        else
        {
            return new ResponseEntity<String>("Operations failed, wrong creditentials", HttpStatus.BAD_REQUEST);
        }
    }
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PutMapping("/myTable")
    ResponseEntity<?> updateMyTable(@Valid @RequestBody MyTable myTable, @RequestHeader String token,
            @RequestHeader String user) {
        log.info("Request to update myTable: {}", myTable);
        UsersList usersList = usersRepository.findByUserName(user);
        if (usersList.getToken().equals(token) && TokenExpiryCheck.tokenExpiryCheck(token) ) {
            MyTable result = myTableRepository.save(myTable);
            return new ResponseEntity<MyTable>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Operations failed, wrong creditentials", HttpStatus.BAD_REQUEST);
        }
    }
    @CrossOrigin(origins = "*", maxAge = 3600)
    @DeleteMapping("/myTable/{id}")
    public ResponseEntity<?> deleteMyTable(@PathVariable Long id, @RequestHeader String token,
    @RequestHeader String user) {
        log.info("Request to delete myTable: {}", id);
        UsersList usersList = usersRepository.findByUserName(user);
        if (usersList.getToken().equals(token) && TokenExpiryCheck.tokenExpiryCheck(token))
        {
        myTableRepository.deleteById(id);
        return ResponseEntity.ok().build();}
        else{
            return new ResponseEntity<String>("Operations failed, wrong creditentials", HttpStatus.BAD_REQUEST);  
        }
    }

}
