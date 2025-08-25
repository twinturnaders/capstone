package wgu.edu.BrinaBright.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import wgu.edu.BrinaBright.DTOs.UpdateAccountRequest;
import wgu.edu.BrinaBright.DTOs.UserDTO;
import wgu.edu.BrinaBright.Security.UserPrincipal;
import wgu.edu.BrinaBright.Services.UserService;



@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDTO getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userService.getUserProfile(userPrincipal.getId());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAccount(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @RequestBody UpdateAccountRequest request) {
        userService.updateUserAccount(userPrincipal.getId(), request);
        return ResponseEntity.ok().build();
    }
}
