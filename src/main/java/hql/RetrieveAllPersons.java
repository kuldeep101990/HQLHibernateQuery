package hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class RetrieveAllPersons {

    public static void main(String[] args) {
        // Set up the Hibernate configuration and SessionFactory
        Configuration configuration = HibernateConfig.getConfig();
        configuration.addAnnotatedClass(Person.class);  // Register Person entity
        configuration.addAnnotatedClass(Address.class); // Register Address entity

        // Create a ServiceRegistry
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(configuration.getProperties())
            .build();
        
        // Build the SessionFactory
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        System.out.println("SessionFactory created successfully.");

        try (Session session = sessionFactory.openSession()) {
            // Start a transaction
            Transaction transaction = session.beginTransaction();
            
            // Execute a query to retrieve all persons
            Query<Person> query = session.createQuery("FROM Person", Person.class);
            List<Person> persons = query.list();
            
            // Print all persons' names
            persons.forEach(person -> System.out.println(person.getName()));
            
            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close();
        }
    }
}
