package ru.kata.spring.boot_security.demo.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.validators.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class AdminController {
    private final UserService userService;

    private final RoleService roleService;

    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

//    @GetMapping("/user")
//    public String showUserInfo(Model model, Principal principal){
//        User user = userService.findByUsername(principal.getName());
//        model.addAttribute("user", user);
//        return "user";
//    }

    @GetMapping("/admin")
    public String showAllUsers(Model model, Principal principal){
        List<User> users = userService.listUsers();
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        model.addAttribute("authUser", user);
        model.addAttribute("users", users);
        return "adminPage";
    }
    @PostMapping("/admin")
    public String addUser(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult,
                          Principal principal, Model model){
        userValidator.validate(user, bindingResult);
//        User authUser = userService.findByUsername(principal.getName());
//        model.addAttribute("authUser", authUser);
        if(bindingResult.hasErrors()){
            return "adminPage"; // TODO!!!!!!!!!!!!!!!!!!
        }
        userService.add(user);
        return "redirect:/users/admin";
    }

//    @GetMapping("/admin/new")
//    public String showFormAddUser(Model model, Principal principal){
//        List<Role> roles = roleService.getAllRoles();
//        model.addAttribute("user", new User());
//        User user = userService.findByUsername(principal.getName());
//        model.addAttribute("thisUser", user);
//        model.addAttribute("allRoles", roles);
//        return "newUser2";
//    }



    @GetMapping("/admin/update")
    public String updateUserForm(Model model, @RequestParam("id") Long id){
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("allRoles", roles);
        model.addAttribute("user", userService.findById(id));
        return "updateUser";
    }

    @PatchMapping("/admin/update")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             Model model){
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()){
            List<Role> roles = roleService.getAllRoles();
            model.addAttribute("allRoles", roles);
            model.addAttribute("user", user);
            return "updateUser";
        }
        userService.update(user);
        return "redirect:/users/admin";
    }

    @DeleteMapping()
    public String deleteUser(@ModelAttribute("user") User user){
        userService.delete(user);
        return "redirect:/users/admin";
    }






}