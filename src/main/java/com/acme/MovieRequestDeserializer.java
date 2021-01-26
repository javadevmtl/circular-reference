package com.acme;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class MovieRequestDeserializer extends ObjectMapperDeserializer<Movie> {
    public MovieRequestDeserializer(){
        // pass the class to the parent.
        super(Movie.class);
    }
}