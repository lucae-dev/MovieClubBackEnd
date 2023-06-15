package com.movies.movie.app.auth;
import com.movies.movie.app.Config.JwtService;
import com.movies.movie.app.Mail.EmailService;
import com.movies.movie.app.Token.Token;
import com.movies.movie.app.Token.TokenRepository;
import com.movies.movie.app.Token.TokenType;
import com.movies.movie.app.user.Role;
import com.movies.movie.app.user.User;
import com.movies.movie.app.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    String thisUrl = "http::localhost:8080";

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        String confirmmsg = "Hi " + user.getUsername() + ", thank you for registering to MovieClub. Click this link to confirm: " + thisUrl +"/confirmRegistration?token=" + jwtToken + ".";
        // send confirmation email!
        System.out.println(user.getEmail() + "!!!!!!+++++!!!!");
        emailService.sendSimpleEmail(user.getEmail(), "MovieClub registration", confirmmsg );


        //non mandare il token come risposta per sicurezza, altrimenti chiunque puo confermare qualunque email
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse confirmRegistration(AuthenticationResponse token){
        var userDetails = userDetailsService
                .loadUserByUsername(
                        jwtService.extractUsername(
                                token.getToken()));

        if(!jwtService.isTokenValid(token.getToken(),userDetails )){
            throw new IllegalStateException("Verification token not valid!");
        }

        var user = repository.findByUsername(jwtService.extractUsername(token.getToken())).orElseThrow();
        user.setVerified(true);
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        //send also link to app already logged in with the token ++++!!!!

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }



}