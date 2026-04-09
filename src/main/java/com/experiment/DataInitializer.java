package com.experiment;

import com.experiment.entity.Category;
import com.experiment.entity.Product;
import com.experiment.entity.Role;
import com.experiment.entity.User;
import com.experiment.repository.CategoryRepository;
import com.experiment.repository.ProductRepository;
import com.experiment.repository.RoleRepository;
import com.experiment.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("=========================================");
        System.out.println("  BOOTSTRAPPING DATA (EXPERIMENT 6)      ");
        System.out.println("=========================================");

        // --- 1. One-to-Many & Many-to-One ---
        Category electronics = new Category("Electronics");
        Product laptop = new Product("Laptop", 1200.0, electronics);
        Product phone = new Product("Smartphone", 800.0, electronics);
        Product tablet = new Product("Tablet", 450.0, electronics);
        Product monitor = new Product("Monitor", 300.0, electronics);
        
        electronics.setProducts(Arrays.asList(laptop, phone, tablet, monitor));
        categoryRepository.save(electronics);

        // --- 2. Many-to-Many ---
        Role adminRole = new Role("ADMIN");
        Role userRole = new Role("USER");
        roleRepository.saveAll(Arrays.asList(adminRole, userRole));

        User alice = new User("Alice");
        alice.getRoles().add(adminRole);
        alice.getRoles().add(userRole);

        User bob = new User("Bob");
        bob.getRoles().add(userRole);
        
        User charlie = new User("Charlie");
        charlie.getRoles().add(userRole);

        userRepository.saveAll(Arrays.asList(alice, bob, charlie));

        System.out.println("=========================================");
        System.out.println("  EXECUTING CUSTOM JPQL QUERIES          ");
        System.out.println("=========================================");

        // --- 3. Pagination, Sorting and Filtering (Products) ---
        // Fetch products between $400 and $1500, sort by price descending, page 0, size 2
        PageRequest productPageReq = PageRequest.of(0, 2, Sort.by("price").descending());
        Page<Product> productPage = productRepository.findProductsByPriceRange(400.0, 1500.0, productPageReq);

        System.out.println("\n[QUERY] Products between $400 and $1500 (Sorted by Price DESC, Max 2 per page):");
        System.out.println("Total ELements Found: " + productPage.getTotalElements());
        for (Product p : productPage.getContent()) {
            System.out.println(" -> " + p.getName() + " : $" + p.getPrice());
        }

        // --- 4. Pagination, Sorting and Filtering (Users by Role) ---
        // Fetch users with role 'USER', sorted by username ascending, page 0, size 5
        PageRequest userPageReq = PageRequest.of(0, 5, Sort.by("username").ascending());
        Page<User> userPage = userRepository.findUsersByRoleName("USER", userPageReq);

        System.out.println("\n[QUERY] Users with 'USER' role (Sorted by Username ASC):");
        System.out.println("Total Elements Found: " + userPage.getTotalElements());
        for (User u : userPage.getContent()) {
            System.out.println(" -> " + u.getUsername());
        }

        System.out.println("=========================================");
        System.out.println("  DATASOURCE INIT COMPLETE               ");
        System.out.println("=========================================");
    }
}
