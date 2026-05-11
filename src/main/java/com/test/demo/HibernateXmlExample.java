package com.test.demo;

import com.test.demo.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HibernateXmlExample implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Starting Early Hibernate XML Example ===");
        
        // 1. Setup SessionFactory
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            
            // 2. Create products
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                
                Product p1 = new Product("Laptop", 1200.0);
                Product p2 = new Product("Smartphone", 800.0);
                
                session.persist(p1);
                session.persist(p2);
                
                transaction.commit();
                System.out.println("Saved products to database!");
            }
            
            // 3. Query products
            try (Session session = sessionFactory.openSession()) {
                Query<Product> query = session.createQuery("from Product", Product.class);
                List<Product> products = query.list();
                
                System.out.println("\n--- Retrieved Products ---");
                for (Product product : products) {
                    System.out.println(product);
                }
                System.out.println("--------------------------\n");
            }
        }
        
        System.out.println("=== Finished Early Hibernate XML Example ===");
    }
}
