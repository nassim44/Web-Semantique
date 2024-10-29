package tn.esprit.twin1.EducationSpringApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import tn.esprit.twin1.EducationSpringApp.servicesJena.EventService;

@EnableScheduling
@EnableAspectJAutoProxy
@EnableAsync
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class TpSpringApplication implements CommandLineRunner {



    @Autowired
    private EventService jena; // Inject the Jena component

    public static void main(String[] args) {
        SpringApplication.run(TpSpringApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {



    }
}
