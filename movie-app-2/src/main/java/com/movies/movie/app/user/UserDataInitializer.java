package com.movies.movie.app.user;

import com.movies.movie.app.MovieCollection.MovieCollection;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component

public class UserDataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDataInitializer(UserRepository userRepository,PasswordEncoder passwordEncoder ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static final Map<Integer, String> usernameDictionary = new HashMap<>();

    static {
        usernameDictionary.put(1, "luca");
        usernameDictionary.put(2, "user");
        usernameDictionary.put(3, "FanCinefilo");
        usernameDictionary.put(4, "SonoNolan");

        // ... add more usernames as needed
    }

    @PostConstruct
    @Transactional
    public void insertDummyUsers() {
        insertUsers(4); // insert 2 users, for example
    }

    @Transactional
    public void insertUsers(int numUsers) {
        for (int i = 1; i <= numUsers; i++) {
            String username = usernameDictionary.get(i);
            if (username != null) {
                User user = User.builder()
                        .username(username)
                        .email(username + "@email.com")
                        .password(passwordEncoder.encode("pass"))
                        .role(Role.USER) // assume Role is an enum you defined
                        .propic("https://picsum.photos/seed/"+username +"/300/300")
                        .biography("Amo i film!")
                        .build();

                MovieCollection seenCollection = new MovieCollection();
                seenCollection.setName("Seen");
                seenCollection.setCreation_date(LocalDateTime.now());
                seenCollection.setOwner(user);
                seenCollection.setVisible(Boolean.TRUE);
                user.setSeenCollection(seenCollection);

                MovieCollection toBeSeenCollection = new MovieCollection();
                toBeSeenCollection.setName("To Be Seen");
                toBeSeenCollection.setCreation_date(LocalDateTime.now());
                toBeSeenCollection.setOwner(user);
                toBeSeenCollection.setVisible(Boolean.TRUE);
                user.setToBeSeenCollection(toBeSeenCollection);

                MovieCollection likedCollection = new MovieCollection();
                likedCollection.setCreation_date(LocalDateTime.now());
                likedCollection.setName("Liked");
                likedCollection.setOwner(user);
                likedCollection.setVisible(Boolean.TRUE);
                user.setLikedCollection(likedCollection);

                if(!userRepository.findByUsername(user.getUsername()).isPresent()){
                    userRepository.save(user);

                }
            }
        }
    }
}
