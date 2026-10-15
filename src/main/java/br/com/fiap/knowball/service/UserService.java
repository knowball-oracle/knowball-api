package br.com.fiap.knowball.service;

import br.com.fiap.knowball.dto.UpdateProfileRequest;
import br.com.fiap.knowball.dto.UpdateUserRequest;
import br.com.fiap.knowball.dto.UserProfileResponse;
import br.com.fiap.knowball.dto.UserResponse;
import br.com.fiap.knowball.exception.EmailAlreadyExistsException;
import br.com.fiap.knowball.model.User;
import br.com.fiap.knowball.repository.UserRepository;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    public Page<UserResponse> getUsersWithFilter(String name, String email, Pageable pageable) {
        return userRepository
                .findByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(
                        name != null ? name : "",
                        email != null ? email : "",
                        pageable)
                .map(this::toResponse);
    }

    public Optional<UserResponse> getUserById(@NonNull Long id) {
        return userRepository.findById(id).map(this::toResponse);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserResponse createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists" + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public Optional<UserResponse> updateUser(
            @NonNull Long id,
            UpdateUserRequest request) {

        return userRepository.findById(id).map(user -> {
            String name = request.name().trim();
            String email = request.email().trim();

            boolean emailChanged = !user.getEmail().equals(email);

            if (emailChanged && userRepository.existsByEmail(email)) {
                throw new EmailAlreadyExistsException(
                        "Email already exists: " + email
                );
            }

            user.setName(name);
            user.setEmail(email);
            user.setRole(request.role());

            String newPassword = request.password();

            if (newPassword != null && !newPassword.isBlank()) {
                user.setPassword(passwordEncoder.encode(newPassword));
            }

            return toResponse(userRepository.save(user));
        });
    }

    public boolean deleteUser(@NonNull Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private UserProfileResponse toProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getProfilePicture()
        );
    }

    public Optional<UserProfileResponse> getProfileByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toProfileResponse);
    }

    public Optional<UserProfileResponse> updateProfile(String email, UpdateProfileRequest request) {
        return userRepository.findByEmail(email).map(user -> {
            if (request.name() != null && !request.name().isBlank()) {
                user.setName(request.name());
            }

            if (request.profilePicture() != null) {
                if (request.profilePicture().isBlank()) {
                    user.setProfilePicture(null);
                } else {
                    user.setProfilePicture(request.profilePicture());
                }
            }

            return toProfileResponse(userRepository.save(user));
        });
    }
}
