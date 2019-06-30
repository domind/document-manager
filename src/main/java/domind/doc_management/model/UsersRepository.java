package domind.doc_management.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<UsersList, Long> {
    UsersList findByUserName(String userName);
}
