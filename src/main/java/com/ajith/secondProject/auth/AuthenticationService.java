package com.ajith.secondProject.auth;
import com.ajith.secondProject.auth.Exceptions.UserBlockedException;
import com.ajith.secondProject.auth.Requests.AuthenticationRequest;
import com.ajith.secondProject.auth.Requests.RegisterRequest;
import com.ajith.secondProject.auth.Response.AuthenticationResponse;
import com.ajith.secondProject.config.JwtService;
import com.ajith.secondProject.user.Role;
import com.ajith.secondProject.user.User;
import com.ajith.secondProject.user.UserRepository;
import com.ajith.secondProject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;
    private  final AuthenticationManager authenticationManager;
    private final UserService userService;
    public AuthenticationResponse register (RegisterRequest request) {
        System.out.println (request );
        var user = User.builder ( )
                .firstName ( request.getFirstName () )
                .lastName ( request.getLastName () )
                .email ( request.getEmail () )
                .password (passwordEncoder.encode (request.getPassword ()))
                .role ( Role.ADMIN )

                .build ();
        userRepository.save ( user );
        var jwtToken = jwtService.generateToken ( user );
        return AuthenticationResponse.builder ( )
                .token(jwtToken)
                .build ( );
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (user.getIsActive ()) {
                throw new UserBlockedException ("User is blocked");
            }
            var jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (BadCredentialsException e) {

            throw new BadCredentialsException ("Password is Wrong");
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

}