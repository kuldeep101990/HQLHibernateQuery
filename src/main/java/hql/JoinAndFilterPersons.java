package hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class JoinAndFilterPersons {

    public static void main(String[] args) {
        // Set up the Hibernate configuration and SessionFactory
        Configuration configuration = HibernateConfig.getConfig();
        configuration.addAnnotatedClass(Person.class); // Register Person entity
        configuration.addAnnotatedClass(Address.class); // Register Address entity

        // Create a ServiceRegistry
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(configuration.getProperties())
            .build();
        
        // Build the SessionFactory
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        System.out.println("SessionFactory created successfully.");

        // Use try-with-resources to automatically close the session
        try (Session session = sessionFactory.openSession()) {
            // Start a transaction
            Transaction transaction = session.beginTransaction();

            // HQL query to join Person with Address and filter by city
            String hql = "FROM Person p JOIN p.addressEntity a WHERE a.city = :city";
            Query<Person> query = session.createQuery(hql, Person.class);
            query.setParameter("city", "New York");

            // Execute the query and get the filtered list of persons
            List<Person> filteredPersons = query.list();
            filteredPersons.forEach(person -> 
                System.out.println("Filtered Person: " + person.getName())
            );

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close(); // Ensure session factory is closed
        }
    }
}
