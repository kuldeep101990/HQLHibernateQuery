package hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.MutationQuery;
import org.hibernate.service.ServiceRegistry;

public class DeletePerson {

    public static void main(String[] args) {
        // Set up the Hibernate configuration and SessionFactory
        Configuration configuration = HibernateConfig.getConfig();
        configuration.addAnnotatedClass(Person.class); // Register Person entity
        configuration.addAnnotatedClass(Address.class); // Register Person entity
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

            // HQL query to delete the person with the given ID
            String hqlDelete = "DELETE FROM Person WHERE id = :id";
            MutationQuery deleteQuery = session.createMutationQuery(hqlDelete);

            // Set the parameter for the delete query
            deleteQuery.setParameter("id", 1);

            // Execute the delete query
            int result = deleteQuery.executeUpdate();
            System.out.println(result + " rows deleted.");

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close(); // Ensure session factory is closed
        }
    }
}
