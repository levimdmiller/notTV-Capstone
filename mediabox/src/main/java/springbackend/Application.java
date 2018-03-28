package springbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import filesharingsystem.PortMapException;
import filesharingsystem.PortMapper;

import util.SeedManager;

@SpringBootApplication
@ImportResource("classpath:app-config.xml")
public class Application implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    @Autowired
    PortMapper                  portMapper;
    @Autowired
    SeedManager                 seedManager;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
    }
}
