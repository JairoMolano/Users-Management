package co.usco.user_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import co.usco.user_management.models.UserModel;
import co.usco.user_management.services.UserService;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email, @RequestParam("postalCode") String postalCode, @RequestParam("birthdate") String birthdate) {
            userService.createUser(username, password, email, postalCode, birthdate);
            return "/register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home/user")
    public String homeUser(Model model, Authentication authentication) {
        model.addAttribute("user", authentication.getUsername());  
        return "usersViews/home";
    }


    @GetMapping("/home/admin")
    public String homeAdmin(Model model, Authentication authentication) {
        model.addAttribute("user", authentication.getUsername());
        model.addAttribute("users", userService.getAllUsersExcludingAdmin());
        return "adminViews/home";
    }

    @GetMapping("/home/admin/create-user")
    public String createUserView() {
        return "adminViews/create-user";
    }

    @PostMapping("/home/admin/create-user")
    public String createUser(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email, @RequestParam("postalCode") String postalCode, @RequestParam("birthdate") String birthdate) {
        userService.createUser(username, password, email, postalCode, birthdate);
        return "redirect:/home/admin";
    }

    @GetMapping("/home/admin/edit-user/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        UserModel user = userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "adminViews/edit-user";
    }

    @PostMapping("/home/admin/edit-user/{id}")
    public String updateUser(@PathVariable Long id, @RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("postalCode") String postalCode, @RequestParam("birthdate") String birthdate) {
        userService.updateUser(id, username, email, postalCode, birthdate);
        return "redirect:/home/admin";
    }

    @GetMapping("/home/admin/confirm-delete-user/{id}")
    public String confirmDeleteUser(@PathVariable Long id, Model model) {
        UserModel user = userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "adminViews/confirm-delete-user";
    }

    @PostMapping("/home/admin/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/home/admin";
    }

}
