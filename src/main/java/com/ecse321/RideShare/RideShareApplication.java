package com.ecse321.RideShare;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController

public class RideShareApplication {
    private static final Logger LOG = LoggerFactory.getLogger(RideShareApplication.class);
  
    public static void main(String[] args) {
		SpringApplication.run(RideShareApplication.class, args);
	}

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @RequestMapping(path="/")
    public String greeting() {
        return "Hello, world! ";
    }
    
    @RequestMapping(path="/users", method=RequestMethod.GET)
    public String user() {
        List<Map<String,Object>> list;
        list = jdbcTemplate.queryForList("select * from user_table");
        return list.toString();
    }
    
    @RequestMapping(path="/trips", method=RequestMethod.GET)
    public String trip() {
        List<Map<String,Object>> list;
        list = jdbcTemplate.queryForList("select * from trip_table");
        return list.toString();
    }

    @RequestMapping(path="/users/{id}", method=RequestMethod.GET)
    public String read(@PathVariable String id) {
        List<Map<String,Object>> list;
        list = jdbcTemplate.queryForList("select * from user_table where userid = ?", id);
        return list.toString();
    }
}

