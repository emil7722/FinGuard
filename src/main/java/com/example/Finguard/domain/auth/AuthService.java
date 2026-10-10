package com.example.Finguard.domain.auth;

import com.example.Finguard.common.exception.ValidationException;
import com.example.Finguard.domain.user.User;
import com.example.Finguard.domain.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthUserRepository authUserRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthUserRepository authUserRepository, UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.authUserRepository = authUserRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthTokenResponse register(RegisterRequest request) {
        logger.info("=== REGISTER START ===");
        logger.info("Registering user: {}", request.getUsername());

        try {
            // Check if username or email already exists
            logger.info("Checking if username exists: {}", request.getUsername());
            if (authUserRepository.findByUsername(request.getUsername()).isPresent()) {
                logger.warn("Username already exists: {}", request.getUsername());
                throw new ValidationException("Username already exists: " + request.getUsername());
            }

            logger.info("Checking if email exists: {}", request.getEmail());
            if (authUserRepository.findByEmail(request.getEmail()).isPresent()) {
                logger.warn("Email already exists: {}", request.getEmail());
                throw new ValidationException("Email already exists: " + request.getEmail());
            }

            // Create new auth user
            logger.info("Creating new AuthUser");
            AuthUser authUser = new AuthUser();
            authUser.setUsername(request.getUsername());
            authUser.setEmail(request.getEmail());
            authUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            authUser.setRole("USER");
            authUser.setEnabled(true);

            logger.info("Saving AuthUser to database");
            AuthUser savedAuthUser = authUserRepository.save(authUser);
            logger.info("AuthUser saved with ID: {}", savedAuthUser.getId());

            // Create corresponding User record
            logger.info("Creating corresponding User record");
            User user = new User();
            user.setId(savedAuthUser.getId());
            user.setEmail(savedAuthUser.getEmail());
            user.setFullName(request.getUsername());
            user.setStatus("ACTIVE");
            userRepository.save(user);
            logger.info("User record created with ID: {}", user.getId());

            // Generate token
            logger.info("Generating JWT token for user: {}", savedAuthUser.getUsername());
            String token = jwtTokenProvider.generateToken(savedAuthUser.getId(), savedAuthUser.getUsername(), savedAuthUser.getRole());
            logger.info("Token generated successfully");

            AuthTokenResponse response = new AuthTokenResponse(token, savedAuthUser.getId(), savedAuthUser.getUsername(), savedAuthUser.getRole());
            logger.info("=== REGISTER SUCCESS ===");
            return response;
        } catch (Exception e) {
            logger.error("=== REGISTER FAILED ===", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public AuthTokenResponse login(LoginRequest request) {
        logger.info("=== LOGIN START ===");
        logger.info("Login attempt for user: {}", request.getUsername());

        try {
            logger.info("Fetching user from database: {}", request.getUsername());
            AuthUser authUser = authUserRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ValidationException("Invalid username or password"));
            logger.info("User found: {}", authUser.getId());

            if (!authUser.getEnabled()) {
                logger.warn("User account is disabled: {}", request.getUsername());
                throw new ValidationException("User account is disabled");
            }

            logger.info("Validating password");
            if (!passwordEncoder.matches(request.getPassword(), authUser.getPasswordHash())) {
                logger.warn("Invalid password for user: {}", request.getUsername());
                throw new ValidationException("Invalid username or password");
            }
            logger.info("Password valid");

            // Generate token
            logger.info("Generating JWT token");
            String token = jwtTokenProvider.generateToken(authUser.getId(), authUser.getUsername(), authUser.getRole());
            logger.info("Token generated successfully");

            AuthTokenResponse response = new AuthTokenResponse(token, authUser.getId(), authUser.getUsername(), authUser.getRole());
            logger.info("=== LOGIN SUCCESS ===");
            return response;
        } catch (Exception e) {
            logger.error("=== LOGIN FAILED ===", e);
            throw e;
        }
    }
}
