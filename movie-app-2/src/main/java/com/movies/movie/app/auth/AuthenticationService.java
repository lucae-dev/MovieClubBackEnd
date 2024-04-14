package com.movies.movie.app.auth;

import com.movies.movie.app.Config.JwtService;
import com.movies.movie.app.Mail.EmailService;
import com.movies.movie.app.Mail.HtmlContent;
import com.movies.movie.app.MovieCollection.MovieCollection;
import com.movies.movie.app.MovieCollection.MovieCollectionType;
import com.movies.movie.app.Token.Token;
import com.movies.movie.app.Token.TokenRepository;
import com.movies.movie.app.Token.TokenType;
import com.movies.movie.app.auth.Bean.AuthenticationRequest;
import com.movies.movie.app.auth.Bean.AuthenticationResponse;
import com.movies.movie.app.auth.Bean.RegisterRequest;
import com.movies.movie.app.auth.Exception.AuthenticationException;
import com.movies.movie.app.auth.Exception.RegistrationException;
import com.movies.movie.app.user.Role;
import com.movies.movie.app.user.User;
import com.movies.movie.app.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;


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
    String thisUrl = "http://localhost:9191/api/v1/auth";

    public AuthenticationResponse register(RegisterRequest request) {

        validateRegistrationRequest(request);

        var user2 = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        var user = repository.save(user2);

        var savedUser = setUpUser(user);

        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        String confirmmsg = "Hi " + user.getUsername() + ", thank you for registering to MovieClub. Click this link to confirm: " + thisUrl +"/confirmRegistration?token=" + jwtToken + ".";
        // send confirmation email!
        HtmlContent htmlContent = new HtmlContent(thisUrl+"/confirmRegistration?token=" + jwtToken, user.getUsername());
       /* try {
            emailService.sendNiceEmail(user.getEmail(), "MovieClub registration", htmlContent.getHtmlContent());
        } catch (MessagingException e) {
            e.printStackTrace();
        }*/
        //emailService.sendSimpleEmail(user.getEmail(), "MovieClub registration", confirmmsg );


        //non mandare il token come risposta per sicurezza, altrimenti chiunque puo confermare qualunque email
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void validateRegistrationRequest(RegisterRequest request) {
        validateRequestFields(request);
        validateRegistrationConsistency(request);
    }

    private static void validateRequestFields(RegisterRequest request) {
        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Password requirements: at least 8 characters, one uppercase, one lowercase, one number, and one special character
        // String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        if (request.getUsername().length() < 4 ) {
            throw new RegistrationException("Username must have at least 4 characters");
        } else if (!Pattern.matches(EMAIL_REGEX, request.getEmail())) {
            throw new RegistrationException("Email not valid");
        } else if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new RegistrationException("Your password should have at least 6 characters");
        }
    }

    private void validateRegistrationConsistency(RegisterRequest request) {
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw new RegistrationException("Username already taken");
        } else if (repository.findUserByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Email already associated to an acocunt");
        } else if (request.getPassword().isBlank() || request.getPassword().isBlank()) {
            throw new RegistrationException("Invalid password");
        }
    }

    private User setUpUser(User user) {
        MovieCollection seenCollection = new MovieCollection();
        seenCollection.setName("Seen");
        seenCollection.setCreation_date(LocalDateTime.now());
        seenCollection.setOwner(user);
        seenCollection.setType(MovieCollectionType.SEEN);
        seenCollection.setVisible(Boolean.TRUE);
        user.setSeenCollection(seenCollection);
        user.setPropic("https://picsum.photos/seed/"+ user.getUsername() +"/300/300");

        MovieCollection toBeSeenCollection = new MovieCollection();
        toBeSeenCollection.setName("To Be Seen");
        toBeSeenCollection.setCreation_date(LocalDateTime.now());
        toBeSeenCollection.setOwner(user);
        toBeSeenCollection.setType(MovieCollectionType.WATCHLIST);
        toBeSeenCollection.setVisible(Boolean.TRUE);
        user.setToBeSeenCollection(toBeSeenCollection);

        MovieCollection likedCollection = new MovieCollection();
        likedCollection.setCreation_date(LocalDateTime.now());
        likedCollection.setName("Liked");
        likedCollection.setOwner(user);
        likedCollection.setType(MovieCollectionType.FAVOURITES);
        likedCollection.setVisible(Boolean.TRUE);
        user.setLikedCollection(likedCollection);

        var savedUser = repository.save(user);
        return savedUser;
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
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            System.out.println("Invalid username or password.");
            throw new AuthenticationException("Invalid username or password");
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            throw new AuthenticationException("Authentication failed, retry");
        }

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Username not found!"));
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