package com.acme;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovieProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(MovieProcessor.class);

    @Incoming("movies-in")
    public void process(Movie movie) {
        LOG.info("Processing movie: {}", movie);

        if(movie.getTitle().startsWith("Dude"))
            throw new IllegalStateException("Sweet!");

        LOG.info("We like watching all kinds of movies.");
    }
}
