package hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class FilterPersonsByName {

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
            
            // Execute a query to filter Person records by name pattern
            Query<Person> query = session.createQuery("FROM Person WHERE name LIKE :name", Person.class);
            query.setParameter("name", "%amit%");
            
            // Get the list of persons whose name matches the pattern
            List<Person> filteredPersons = query.list();
            
            // Print the filtered persons
            filteredPersons.forEach(person -> System.out.println("Filtered Person: " + person.getName()));
            
            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close();
        }
    }
}
