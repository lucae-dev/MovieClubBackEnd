package com.movies.movie.app.Search;

import com.movies.movie.app.MovieCollection.MovieCollection;
import com.movies.movie.app.MovieCollection.MovieCollectionDTO;
import com.movies.movie.app.MovieCollection.MovieCollectionRepository;
import com.movies.movie.app.MovieCollection.MovieCollectionService;
import com.movies.movie.app.movie.MovieDTO;
import com.movies.movie.app.movie.MovieService;
import com.movies.movie.app.user.User;
import com.movies.movie.app.user.UserDTO;
import com.movies.movie.app.user.UserRepository;
import com.movies.movie.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final MovieCollectionService movieCollectionService;
    private final UserService userService;
    private final MovieService movieService;

    @Autowired
    public SearchController(MovieCollectionService movieCollectionService, UserService userService, MovieService movieService) {
        this.movieCollectionService = movieCollectionService;
        this.userService = userService;
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<?> search(@AuthenticationPrincipal User user,
            @RequestParam String keyword,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "it-IT") String region,
                                    @RequestParam(defaultValue = "en-US") String language) {
        Pageable pageable =  PageRequest.of(page, size);
        Map<String, Object> results = new HashMap<>();

        if (keyword == null || keyword.isBlank() || keyword.isEmpty()) {
            results.put("movieCollections",  new ArrayList<>());
            results.put("users",  new ArrayList<>());
            return ResponseEntity.ok(results);
        }

        String Keyword;
        try {
            Keyword = URLDecoder.decode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return ResponseEntity.badRequest().body("error in decoding query");
        }

        System.out.println(Keyword);
        List<MovieCollectionDTO> movieCollections =  movieCollectionService.searchByKeyword(user, Keyword, pageable);
        List<UserDTO> users = userService.searchByKeyword(user, Keyword, pageable);
        // List<MovieDTO> movies = movieService.searchMovies(user,Keyword, "", UriUtils.decode(region, "UTF-8"), language);
        results.put("movieCollections", movieCollections);
        results.put("users", users);

        return ResponseEntity.ok(results);
    }
}
