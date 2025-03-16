package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequest;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequest.ApprovalStatus;
import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionRequest, Integer> {
    Optional<PermissionRequest> findByRequestId(Integer requestId);
    List<PermissionRequest> findByUser(User user);
    Optional<PermissionRequest> findById(Integer requestId);
    List<PermissionRequest> findByStatus(ApprovalStatus pending);
}