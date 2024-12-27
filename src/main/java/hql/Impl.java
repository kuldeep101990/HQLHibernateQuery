package hql;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.MutationQuery;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Impl {

  public static void main(String[] args) {
  // Load the configuration and build the SessionFactory
  Configuration configuration = HibernateConfig.getConfig();
  configuration.addAnnotatedClass(Person.class);

  ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
  		.applySettings(configuration.getProperties())
  		.build();
  SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
  System.out.println("SessionFactory created successfully.");
  // Create a session
  Session session = sessionFactory.openSession();
  Transaction transaction = session.beginTransaction();

  
  try {
	// 1. Retrieving all objects of a certain type from the database
      Query<Person> query1 = session.createQuery("FROM Person", Person.class);
      List<Person> persons = query1.list();
      persons.forEach(person -> System.out.println(person.getName()));

      // 2. Retrieving a single object based on its ID
      Query<Person> query2 = session.createQuery("FROM Person WHERE id = :id", Person.class);
      query2.setParameter("id", 1);
      Person person = query2.uniqueResult();
      System.out.println("Person with ID 1: " + person.getName());

      // 3. Updating an object in the database
      MutationQuery updateQuery = session.createMutationQuery("UPDATE Person SET name = :name WHERE id = :id");
      updateQuery.setParameter("name", "Updated Name");
      updateQuery.setParameter("id", 1);
      int result = updateQuery.executeUpdate();
      System.out.println(result + " rows updated.");

      // 4. Deleting an object from the database
      MutationQuery deleteQuery = session.createMutationQuery("DELETE FROM Person WHERE id = :id");
      deleteQuery.setParameter("id", 2);
      result = deleteQuery.executeUpdate();
      System.out.println(result + " rows deleted.");

      // 5. Filtering objects based on a certain property
      Query<Person> query3 = session.createQuery("FROM Person WHERE name LIKE :name", Person.class);
      query3.setParameter("name", "%John%");
      List<Person> filteredPersons = query3.list();
      filteredPersons.forEach(p -> System.out.println("Filtered Person: " + p.getName()));

      // 6. Filtering objects based on a range of values (BETWEEN operator)
      Query<Person> query4 = session.createQuery("FROM Person WHERE id BETWEEN :startId AND :endId", Person.class);
      query4.setParameter("startId", 1);
      query4.setParameter("endId", 5);
      List<Person> rangePersons = query4.list();
      rangePersons.forEach(p -> System.out.println("Range Person: " + p.getName()));

      // 7. Retrieving a count of objects (SELECT COUNT(*))
      Query<Long> countQuery = session.createQuery("SELECT COUNT(*) FROM Person", Long.class);
      Long count = countQuery.uniqueResult();
      System.out.println("Total Person count: " + count);

      // 8. Retrieving an aggregate value (MAX(), AVG())
      Query<Integer> maxQuery = session.createQuery("SELECT MAX(id) FROM Person", Integer.class);
      Integer maxId = maxQuery.uniqueResult();
      System.out.println("Max ID: " + maxId);

      // 9. Retrieving specific properties of objects as a DTO
      Query<Object[]> dtoQuery = session.createQuery("SELECT p.name, p.address FROM Person p", Object[].class);
      List<Object[]> dtos = dtoQuery.list();
      dtos.forEach(dto -> System.out.println("Name: " + dto[0] + ", Address: " + dto[1]));

      transaction.commit();
  } catch (Exception e) {
      if (transaction != null) transaction.rollback();
      e.printStackTrace();
  } finally {
      session.close();
      sessionFactory.close();
  }
}
}