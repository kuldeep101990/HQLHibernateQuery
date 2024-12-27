package hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

public class RetrievePersonById {

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
            
            // Execute a query to retrieve the person with a specific ID
            Query<Person> query = session.createQuery("FROM Person p LEFT JOIN FETCH p.addressEntity WHERE p.id = :id", Person.class);
            query.setParameter("id", 1);
            Person person = query.uniqueResult();
            
            // If the person is found, print their details and address
            if (person != null) {
                System.out.println("Person with ID 1: " + person.getName());
                if (person.getAddressEntity() != null) {
                    System.out.println("Address: " + person.getAddressEntity().getStreet() + ", " 
                                       + person.getAddressEntity().getCity());
                } else {
                    System.out.println("No address found for this person.");
                }
            } else {
                System.out.println("Person not found.");
            }
            
            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close();
        }
    }
}
