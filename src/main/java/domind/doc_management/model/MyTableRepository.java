package domind.doc_management.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyTableRepository extends JpaRepository<MyTable, Long> {
    MyTable findByDescription(String description);
}
