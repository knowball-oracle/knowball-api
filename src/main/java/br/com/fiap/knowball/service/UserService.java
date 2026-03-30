package br.com.fiap.knowball.service;

import java.util.List;
import java.util.Optional;

import br.com.fiap.knowball.dto.UserResponse;
import br.com.fiap.knowball.exception.EmailAlreadyExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.User;
import br.com.fiap.knowball.repository.UserRepository;
import lombok.NonNull;

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

    public Optional<UserResponse> updateUser(@NonNull Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());

            if (!user.getEmail().equals(userDetails.getEmail()) &&
                    userRepository.existsByEmail(userDetails.getEmail())) {
                throw new EmailAlreadyExistsException("Email already exists: " + userDetails.getEmail());
            }
            user.setEmail(userDetails.getEmail());

            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }

            user.setRole(userDetails.getRole());
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
}
