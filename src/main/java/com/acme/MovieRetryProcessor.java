package com.acme;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovieRetryProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(MovieRetryProcessor.class);

    @SuppressWarnings("unchecked")
    @Incoming("movies-retry")
    @Outgoing("movies-back")
    public Uni<Message<Movie>> retry(Message<Movie> in) {
        LOG.info("Retrying movie: {}", in.getPayload());

        // Lets pretend we did everything we could for this movie. Force a fiallure it should reach the end of the line in the DLQ.
        // We will not process the DLQ.
        // This will cause CIRCULAR REFERENCE
        return Uni.createFrom().failure(new IllegalStateException("We tried everything in our power to like this movie. Unfortunately we do not want to watch it any more!"));
    }
}
