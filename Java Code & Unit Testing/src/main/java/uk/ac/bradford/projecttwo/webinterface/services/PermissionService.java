package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.stereotype.Service; // For @Service annotation
import org.springframework.beans.factory.annotation.Autowired; // For dependency injection
import org.springframework.transaction.annotation.Transactional; // For @Transactional annotation
import java.time.LocalDateTime; // For date/time handling
import java.util.List; // For returning lists of requests
import java.util.Set; // For handling sets of datasets
import java.util.HashSet; // For initializing sets
import org.springframework.data.jpa.repository.JpaRepository; // For repository interface
import org.springframework.security.core.userdetails.User;

import jakarta.persistence.*; // For JPA annotations
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequest;
import uk.ac.bradford.projecttwo.webinterface.models.Role;
import uk.ac.bradford.projecttwo.webinterface.repositories.PermissionRepository;
import uk.ac.bradford.projecttwo.webinterface.repositories.UserRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository, UserRepository userRepository) {
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void updateRequestStatus(Integer requestId, PermissionRequest.ApprovalStatus status) {
        PermissionRequest request = permissionRepository.findByRequestId(requestId)
    .orElseThrow(() -> new IllegalArgumentException("Invalid request ID"));
        
        if(request.getStatus() == PermissionRequest.ApprovalStatus.PENDING) {
            request.setStatus(status);
            request.setDecisionDate(LocalDateTime.now());
            
            if (status == PermissionRequest.ApprovalStatus.APPROVED) {
                // Get the user and the requested role
                uk.ac.bradford.projecttwo.webinterface.models.User user = request.getUser();
                Role requestedRole = request.getRequestedRole();
    
                // Add the role to the user
                user.addRole(requestedRole);
    
                // Save the updated user
                userRepository.save(user);
            }
        }
        permissionRepository.save(request);
    }

    public List<PermissionRequest> getPendingRequests() {
        return permissionRepository.findByStatus(PermissionRequest.ApprovalStatus.PENDING);
    }


    public Object getAllRequests() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllRequests'");
    }
}