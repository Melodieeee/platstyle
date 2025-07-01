package com.example.platstyle;

import com.example.platstyle.entities.AppUser;
import com.example.platstyle.entities.Customer;
import com.example.platstyle.entities.Service;
import com.example.platstyle.entities.Stylist;
import com.example.platstyle.repositories.CustomerRepository;
import com.example.platstyle.repositories.ServiceRepository;
import com.example.platstyle.repositories.StylistRepository;
import com.example.platstyle.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class PlatStyleApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatStyleApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository,
                                        CustomerRepository customerRepository,
                                        StylistRepository stylistRepository,
                                        ServiceRepository serviceRepository ){
        return args -> {
            if(userRepository.count()==0) {
                // Admin
                AppUser appUser = new AppUser(null, "Admin", "Admin",new BCryptPasswordEncoder().encode("admin"), new Date(), "admin@gmail.com","M", "6041231235", "ROLE_ADMIN", null, null, null);
                userRepository.save(appUser);
                userRepository.flush();
                Customer customer = new Customer();
                customer.setUid(appUser.getUid());
                customerRepository.save(customer);
                // Stylist with user role
                appUser = new AppUser(null, "Stylist", "Chen",new BCryptPasswordEncoder().encode("stylist"), new Date(), "stylist@gmail.com","M", "6041231235", "ROLE_USER", null, null, null);
                userRepository.save(appUser);
                userRepository.flush();
                customer = new Customer();
                customer.setUid(appUser.getUid());
                customerRepository.save(customer);
                // Stylist with stylist role
                appUser = new AppUser(null, "Stylist2", "Yu",new BCryptPasswordEncoder().encode("stylist"), new Date(), "stylist2@gmail.com","M", "6041231235", "ROLE_STYLIST", null, null, null);
                userRepository.save(appUser);
                userRepository.flush();
                customer = new Customer();
                customer.setUid(appUser.getUid());
                Stylist stylist = new Stylist(null, appUser, appUser.getFirstName()+" "+ appUser.getLastName(), appUser.getPhone(), appUser.getEmail(), "Vancouver", "id.jpg", "workfile.jpg", "../img/avatar7.png", true,0.0,null, null);
                stylistRepository.save(stylist);
                customerRepository.save(customer);
                Service service = new Service(null, "M", "HairCut", 20, 50, appUser, null);
                serviceRepository.save(service);
                service = new Service(null, "F", "HairCut", 30, 60, appUser, null);
                serviceRepository.save(service);
                // user
                appUser = new AppUser(null, "User", "Yang",new BCryptPasswordEncoder().encode("user"), new Date(), "user@gmail.com","M", "6041231235", "ROLE_USER", null, null, null);
                userRepository.save(appUser);
                userRepository.flush();
                customer = new Customer();
                customer.setUid(appUser.getUid());
                customerRepository.save(customer);
            }
        };
    }
}
