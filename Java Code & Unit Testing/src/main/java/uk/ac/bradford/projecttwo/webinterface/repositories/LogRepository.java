package uk.ac.bradford.projecttwo.webinterface.repositories;
 // Adjust the package name as needed

import uk.ac.bradford.projecttwo.webinterface.models.Log;
// Import your Log entity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
    @Query("SELECT l FROM Log l JOIN FETCH l.user ORDER BY l.timestamp DESC")
    List<Log> findAllWithUser();
}