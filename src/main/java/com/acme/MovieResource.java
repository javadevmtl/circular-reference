package com.acme;

import io.smallrye.reactive.messaging.kafka.OutgoingKafkaRecordMetadata;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/movies")
public class MovieResource {
    private static final Logger LOG = LoggerFactory.getLogger(MovieResource.class);

    @Inject
    @Channel("movies-out")
    Emitter<Movie> moEmitter;

    @Path("/action/_add")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public void mo(Movie movie) {
        LOG.info("Received movie: {}", movie);

        OutgoingKafkaRecordMetadata<?> metadata = OutgoingKafkaRecordMetadata.builder()
                .withKey(movie.getTag())
                .build();

        moEmitter.send(Message.of(movie).addMetadata(metadata));
    }
}