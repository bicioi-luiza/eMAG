package com.example.demo.controllers;
import com.example.demo.dtos.UserDTO;
import com.example.demo.entities.User;
import com.example.demo.services.ProductService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
//@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    /**
     *
     * @return
     */
    @GetMapping("/users")
    public ModelAndView getAllUsers() {
        ModelAndView mav=new ModelAndView("users");
        List<UserDTO> dtos = userService.getAllUsers();
        mav.addObject("users",dtos);
        return mav;
    }

    /**
     * Retrieves a user by ID.
     * @param id The ID of the user to retrieve
     * @return ResponseEntity containing the UserDTO if found, or HTTP status NOT_FOUND if not found
     */
    @GetMapping("/userOrderStatus")
    public ModelAndView getUserByIdOrderStatus(@RequestParam("id") Long id) {
        return getUserById(id, "orderStatus");
    }

    @GetMapping("/user")
    public ModelAndView getUserById(@RequestParam("id") Long id) {
        return getUserById(id, "users");
    }

    @GetMapping("/customerInfo")
    public ModelAndView getUserByIdCustomer(@RequestParam("id") Long id) {
        return getUserById(id, "customerCart");
    }

    @GetMapping("/userUpdateDetails")
    public ModelAndView getUserByIdForUpdate(@RequestParam("id") Long id) {
        return getUserById(id, "userUpdate");
    }
    public ModelAndView getUserById(@RequestParam("id") Long id,String viewName) {
        ModelAndView mav=new ModelAndView(viewName);

        try {
            UserDTO userDTO = userService.getUserById(id);
            mav.addObject("users",userDTO);
            return mav;
        } catch (Exception e) {
            mav.addObject("error","Userul cu id-ul respectiv nu exista");
            return mav;
        }
    }

    @GetMapping("/userCreate")
    public String createUserPage(){return "userCreate";};

    /**
     *Creates a new user.
     * @param userDTO The UserDTO containing the user data to create
     * @return ResponseEntity containing the created UserDTO and HTTP status CREATED
     */
    @PostMapping( "/userCreate")
    public ModelAndView createUser( @ModelAttribute UserDTO userDTO) {
        ModelAndView mav=new ModelAndView("userCreate");
        UserDTO createdUser = userService.createUser(userDTO);

        //mav.addObject("user",userDTO);
        mav.addObject("userId", createdUser.getId());
        return mav;
    }
    @GetMapping("/userUpdate")
    public String updateUserPage(){return "userUpdate";};
    /*@GetMapping("/userUpdate/{id}")
    public String updateUserPageBYID(){return "userUpdate";};*/
    /**
     *Updates an existing user.
     * @param id The ID of the user to update
     * @param userDTO The updated UserDTO containing the new user data
     * @return ResponseEntity containing the updated UserDTO if successful, or HTTP status NOT_FOUND if user not found
     */
    @PostMapping("/userUpdate")
    public ModelAndView updateUser(@RequestParam("id") Long id, @Valid @ModelAttribute UserDTO userDTO) {
        ModelAndView mav=new ModelAndView("userUpdate");
        try {
            UserDTO updatedUser = userService.updateUser(id,userDTO);
            mav.addObject("user",updatedUser);
            return mav;
        } catch (Exception e) {
             mav.addObject("error","Userul cu id-ul respectiv nu exista");
            return mav;
        }
    }
    @GetMapping("/userDelete")
    public String deleteUserPage(){return "userDelete";};
    /**
     *
     * @param id The ID of the user to delete
     * @return ResponseEntity with HTTP status OK if successful, or HTTP status NOT_FOUND if user not found
     */
    @PostMapping("/userDelete")
    public ModelAndView deleteUser(@RequestParam("id")  Long id) {
        ModelAndView mav=new ModelAndView("userDelete");
        try {
            userService.deleteUser(id);
            return  mav.addObject("ok","Userul cu id-ul respectiv s-a sters");
        } catch (Exception e) {
            return  mav.addObject("error","Userul cu id-ul respectiv nu exista");
        }
    }
    @Autowired
    private HttpSession session;
    @GetMapping("/login/client")
    public String createLoginCustomerPage(){return "login";};
    @PostMapping("/login/client")
    public ModelAndView clientLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            UserDTO loggedInUser = userService.clientLogin(username,password);
            modelAndView.setViewName("redirect:/products/customer");
            modelAndView.addObject("user", loggedInUser);
            session.setAttribute("loggedInUser", loggedInUser);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Client login failed: {}", e.getMessage());
            modelAndView.setViewName("login");
            modelAndView.addObject("error", "Incorrect username or password");
        }
        return modelAndView;
    }
    @GetMapping("/login/admin")
    public String createLoginAdminPage(){return "loginAdmin";};
    @PostMapping("/login/admin")
    public ModelAndView adminLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            UserDTO loggedInUser = userService.adminLogin(username,password);
            modelAndView.setViewName("redirect:/admin");
            modelAndView.addObject("user", loggedInUser);
            session.setAttribute("loggedInUser", loggedInUser);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Admin login failed: {}", e.getMessage());
            modelAndView.setViewName("login");
            modelAndView.addObject("error", "Incorrect username or password");
        }
        return modelAndView;
    }

}
