package hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.MutationQuery;
import org.hibernate.service.ServiceRegistry;

import java.util.ArrayList;
import java.util.List;

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

            // Multiple address data
            String[][] addressData = {
                {"Noida", "sector 21"},
                {"Delhi", "Laxminagar"},
                {"Gurugram", "DLF"}
            };

            List<Integer> addressIds = new ArrayList<>();

            // Insert multiple addresses using HQL
            for (String[] address : addressData) {
                String insertAddressHql = "INSERT INTO Address (city, street) VALUES (:city, :street)";
                MutationQuery addressQuery = session.createMutationQuery(insertAddressHql);
                addressQuery.setParameter("city", address[0]);
                addressQuery.setParameter("street", address[1]);
                addressQuery.executeUpdate();

                // Fetch the generated Address ID
                String selectAddressIdHql = "SELECT id FROM Address WHERE city = :city AND street = :street";
                Integer addressId = session.createQuery(selectAddressIdHql, Integer.class)
                                           .setParameter("city", address[0])
                                           .setParameter("street", address[1])
                                           .setMaxResults(1)
                                           .uniqueResult();
                addressIds.add(addressId);
                System.out.println("Address saved with ID: " + addressId);
            }

            // Multiple person data
            String[] personNames = {"Amit", "Sumit", "Namit"};

            // Insert multiple persons using HQL, linking them to addresses
            for (int i = 0; i < personNames.length; i++) {
                String insertPersonHql = "INSERT INTO Person (name, addressEntity) VALUES (:name, :addressId)";
                MutationQuery personQuery = session.createMutationQuery(insertPersonHql);
                personQuery.setParameter("name", personNames[i]);
                personQuery.setParameter("addressId", addressIds.get(i)); // Assign each person a different address
                int personResult = personQuery.executeUpdate();
                System.out.println(personResult + " Person record inserted: " + personNames[i]);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close();
        }
    }
}
