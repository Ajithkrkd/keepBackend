package com.ajith.secondProject.auth;
import com.ajith.secondProject.auth.Exceptions.UserBlockedException;
import com.ajith.secondProject.auth.Requests.AuthenticationRequest;
import com.ajith.secondProject.auth.Requests.RegisterRequest;
import com.ajith.secondProject.auth.Response.AuthenticationResponse;
import com.ajith.secondProject.user.Exceptions.CustomAuthenticationException;
import com.ajith.secondProject.user.Requests.UserDetailsUpdateRequest;
import com.ajith.secondProject.user.Response.UserDetailsResponse;
import com.ajith.secondProject.user.UserRepository;
import com.ajith.secondProject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping ("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;
    private  final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity< AuthenticationResponse > register(@RequestBody RegisterRequest request) {
        try {
            boolean existEmail = userService.isEmailExist(request.getEmail());
            if (existEmail) {
                // Email already exists, return an error response
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(AuthenticationResponse.builder()
                                .error("Email already exists")
                                .build());
            }

            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthenticationResponse.builder()
                            .error("An error occurred during registration")
                            .build());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity < ? > register(
            @RequestBody AuthenticationRequest request
    ){
        try {
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        }
        catch (UsernameNotFoundException e) {
            return ResponseEntity.status(403).body("User not found");
        }
        catch (UserBlockedException e) {
            return ResponseEntity.status(403).body("user is blocked");
        }

        catch (BadCredentialsException e) {
            return ResponseEntity.status(403).body("Invalid email or password");
        }  catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping ("/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader ("Authorization") String token) {
        try {

            UserDetailsResponse userDetails = userService.getUserDetails ( token.substring ( 7 ) );
            return ResponseEntity.ok ( userDetails );
        } catch (CustomAuthenticationException e) {
            return ResponseEntity.status ( HttpStatus.UNAUTHORIZED ).body ( e.getMessage ( ) );
        }
    }


    @PostMapping ("/update-user")
    public ResponseEntity<?> updateUserDetails(
            @RequestHeader ("Authorization") String token,
            @RequestBody UserDetailsUpdateRequest userDetailsUpdateRequest
    ){
        try {
            userService.updateUserDetails(token, userDetailsUpdateRequest);
            return ResponseEntity.ok("User details updated successfully");
        } catch (CustomAuthenticationException e) {
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }



}