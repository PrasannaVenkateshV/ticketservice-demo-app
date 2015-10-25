package com.ticketservice;

import com.ticketservice.domain.Seat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {

        log.info("Creating tables");

        jdbcTemplate.execute("DROP TABLE Seat IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE Seat(" +
                "id SERIAL, levelId VARCHAR(255), levelName VARCHAR(255))");

        // Split up the array of whole names into an array of first/last names
        List<Object[]> splitUpNames = Arrays.asList("1 orchestra", "2 balcony", "3 window").stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        // Use a Java 8 stream to print out each tuple of the list
        splitUpNames.forEach(name -> log.info(String.format("Inserting seat record for %s %s", name[0], name[1])));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO seat(levelId, levelName) VALUES (?,?)", splitUpNames);

        log.info("Querying for customer records where levelName = 'balcony':");
        jdbcTemplate.query(
                "SELECT id, levelId, levelName FROM seat WHERE levelName = ?", new Object[] { "balcony" },
                (rs, rowNum) -> new Seat(rs.getInt("id"), rs.getInt("levelId"), rs.getString("levelName"))
        ).forEach(seat -> log.info(seat.toString()));
    }
}