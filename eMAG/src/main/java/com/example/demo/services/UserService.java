package com.example.demo.services;
import com.example.demo.config.RestSendingConfig;
import com.example.demo.dtos.UserDTO;
import com.example.demo.dtos.builders.UserBuilder;
import com.example.demo.entities.EmailRequest;
import com.example.demo.entities.User;

import com.example.demo.repositories.ProductRepository;
import com.example.demo.validators.UserValidator;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * This class represents a service layer for user-related operations.
 * It provides methods for retrieving, creating, updating, and deleting user entities.
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RestSendingConfig restSendingConfig;
    private final UserValidator userValidator;
    @Autowired
    public UserService(UserRepository userRepository,RestSendingConfig restSendingConfig,UserValidator userValidator) {
        this.userRepository = userRepository;
        this.restSendingConfig=restSendingConfig;
        this.userValidator = userValidator;
    }

    /**
     * Retrieves a list of all users.
     * @return a list of all users
     */
    public List<UserDTO> getAllUsers() {
        List<User> personList = userRepository.findAll();
        return personList.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }

    /**
     *Retrieves a user by their ID.
     * @param id is the id of the user to retrieve
     * @return the UserDTO representing the retrieved user
     * @throws Exception
     */
    public UserDTO getUserById(Long id) throws Exception{
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);

            throw new Exception(User.class.getSimpleName() + " with id: " + id);
        }

        return UserBuilder.toUserDTO(user.get());
    }

    /**
     *Creates a new user.
     * @param userDTO is the user that we want to create
     * @return the actual created user
     */
    public UserDTO createUser(UserDTO userDTO) {
        UserValidator.validateUser(userDTO);
        User user = UserBuilder.toEntity(userDTO);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getId());

        //email
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setId(user.getId());
        emailRequest.setFirstName(userDTO.getFirstName());
        emailRequest.setLastName(userDTO.getLastName());
        emailRequest.setRecipientEmail(userDTO.getEmail()); // Assuming email is stored in UserDTO
        emailRequest.setSubject("Welcome to Our Platform"); // Set your subject here
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("<h1>Dear ").append(userDTO.getFirstName()).append(",</h1><br>");
        bodyBuilder.append("<h1>Welcome to our platform!</h1><br>");
        emailRequest.setBody(bodyBuilder.toString());
        // Send the email request to the email service
        restSendingConfig.sendEmailToMailApp(emailRequest);
        return UserBuilder.toUserDTO(user);
    }

    /**
     * Updates an existing user with the given ID.
     * @param id the id of the user we need to update
     * @param userDTO representing the updated user data
     * @return the UserDTO representing the updated user
     * @throws Exception if the user with the given ID is not found
     */
    public UserDTO updateUser(Long id, UserDTO userDTO) throws Exception {
        UserValidator.validateUser(userDTO);
        userValidator.validateUserId(id);
        Optional<User> optUser = userRepository.findById(id);
        User existingUser = optUser.get();

        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setPassword(userDTO.getPassword());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setDateOfBirth(userDTO.getDateOfBirth());
        existingUser.setAdmin(userDTO.isAdmin());

        User updatedUser = userRepository.save(existingUser);

        return UserBuilder.toUserDTO(updatedUser);
    }

    /**
     *Deletes a user with the specified ID.
     * @param id the ID of the user to delete
     * @throws Exception if the user with the given ID is not found
     */
    public void deleteUser(Long id) throws Exception {
        userValidator.validateUserId(id);
        Optional<User> optUser = userRepository.findById(id);
        userRepository.deleteById(id);
    }

    /**
     * login part
     */
    public UserDTO clientLogin(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username,password);
        if (user != null && !user.isAdmin()) {
            storeUserInSession(user);
            return UserBuilder.toUserDTO(user);
        } else {
            LOGGER.error("Client login failed: Incorrect username or password");
            throw new IllegalArgumentException("Incorrect username or password");
        }
    }

    public UserDTO adminLogin(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user != null && user.isAdmin()) {
            storeUserInSession(user);
            return UserBuilder.toUserDTO(user);
        } else {
            LOGGER.error("Admin login failed: Incorrect username or password");
            throw new IllegalArgumentException("Incorrect username or password");
        }
    }

    private void storeUserInSession(User user) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        session.setAttribute("loggedInUser", user);
    }


}
