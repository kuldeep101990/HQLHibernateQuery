package hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.MutationQuery;
import org.hibernate.service.ServiceRegistry;

public class SavePersonUsingQuery {

    public static void main(String[] args) {
        // Set up the Hibernate configuration and SessionFactory
        Configuration configuration = HibernateConfig.getConfig();
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(Address.class); // Add Address class for mapping

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(configuration.getProperties())
            .build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        System.out.println("SessionFactory created successfully.");

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            // Create an Address object
            Address address = new Address();
            address.setCity("New York");
            address.setStreet("1234 Elm Street");

            // Save the Address using an HQL INSERT query
            String insertAddressHql = "INSERT INTO Address (city, street) VALUES (:city, :street)";
            MutationQuery addressQuery = session.createMutationQuery(insertAddressHql);
            addressQuery.setParameter("city", address.getCity());
            addressQuery.setParameter("street", address.getStreet());
            int addressResult = addressQuery.executeUpdate();
            System.out.println(addressResult + " Address record inserted.");

            // Create a Person object
            Person person = new Person();
            person.setName("John Doe");
            person.setAddressEntity(address); // Associate the Address object with the Person

            // Save the Person using an HQL INSERT query
            String insertPersonHql = "INSERT INTO Person (name, addressEntity) VALUES (:name, :address)";
            MutationQuery personQuery = session.createMutationQuery(insertPersonHql);
            personQuery.setParameter("name", person.getName());
            personQuery.setParameter("address", address); // Pass the associated Address object
            int personResult = personQuery.executeUpdate();
            System.out.println(personResult + " Person record inserted.");

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close();
        }
    }
}
