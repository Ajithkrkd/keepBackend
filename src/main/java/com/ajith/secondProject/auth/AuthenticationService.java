package com.ajith.secondProject.auth;
import com.ajith.secondProject.auth.Exceptions.UserBlockedException;
import com.ajith.secondProject.auth.Requests.AuthenticationRequest;
import com.ajith.secondProject.auth.Requests.RegisterRequest;
import com.ajith.secondProject.auth.Response.AuthenticationResponse;
import com.ajith.secondProject.config.JwtService;
import com.ajith.secondProject.token.Token;
import com.ajith.secondProject.token.TokenRepository;
import com.ajith.secondProject.token.TokenType;
import com.ajith.secondProject.user.Role;
import com.ajith.secondProject.user.entity.User;
import com.ajith.secondProject.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;
    private  final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    public AuthenticationResponse register (RegisterRequest request) {
        System.out.println (request );
        var user = User.builder ( )
                .firstName ( request.getFirstName () )
                .lastName ( request.getLastName () )
                .email ( request.getEmail () )
                .password (passwordEncoder.encode (request.getPassword ()))
                .role (  Role.ADMIN )
                .build ();
        User savedUser = userRepository.save ( user );
        var jwtToken = jwtService.generateToken ( user );
        var refreshToken = jwtService.generateRefreshToken ( user );
        saveUserToken ( savedUser, refreshToken );


        return AuthenticationResponse.builder ( )
                .accessToken (jwtToken)
                .refreshToken ( refreshToken )
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
            var refreshToken = jwtService.generateRefreshToken ( user );

            revokeAllTokens ( user );
            saveUserToken ( user, refreshToken );


            return AuthenticationResponse.builder()
                    .accessToken (jwtToken)
                    .refreshToken ( refreshToken )
                    .build();
        } catch (BadCredentialsException e) {

            throw new BadCredentialsException ("Password is Wrong");
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public void revokeAllTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser ( user.getId () );
        if(validUserTokens.isEmpty()) {
            return;
        }
        else{
                validUserTokens.forEach ( t ->{
                    t.setRevoked ( true );
                    t.setExpired ( true );}
                 );
            System.out.println ("Valid ---------------------------" );
                tokenRepository.saveAll ( validUserTokens );
        }
    }


    public AuthenticationResponse refreshToken (
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        final  String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return AuthenticationResponse.builder()
                    .error ( "Invalid Authorization header" )
                    .build();
        }


        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if(userEmail != null ){

             Optional <User> existingUser = userRepository.findByEmail ( userEmail );
                if(existingUser.isPresent ()){
                    User user = existingUser.get();

                    var isTokenValid = tokenRepository.findByToken ( refreshToken ).
                            map ( token -> token.isRefreshToken ( ) &&
                                    !token.isRevoked () &&
                                    !token.isExpired () )
                            .orElse ( false );


                    if(jwtService.isTokenValid ( refreshToken, user ) && isTokenValid ){
                      var accessToken = jwtService.generateToken ( user );
                      var newRefreshToken = jwtService.generateRefreshToken(user);

                          revokeAllTokens ( user );
                          saveUserToken ( user,newRefreshToken );

                          return AuthenticationResponse.builder ()
                                  .refreshToken ( newRefreshToken )
                                  .accessToken ( accessToken )
                                  .build ();

                    }
                }
        }

        return AuthenticationResponse.builder()
                .error("Token is not valid Please Login or Register")
        .build();
    }
    private void saveUserToken (User user, String jwtToken) {
        var token = Token.builder ( )
                .user ( user )
                .token ( jwtToken )
                .tokenType ( TokenType.BEARER )
                .isRefreshToken ( true )
                .expired ( false )
                .revoked ( false )
                .build ();
        tokenRepository.save ( token );
    }
}