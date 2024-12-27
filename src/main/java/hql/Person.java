package hql;

import jakarta.persistence.*;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "address_id") // Foreign key to the Address table
    private Address addressEntity;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(Address addressEntity) {
        this.addressEntity = addressEntity;
    }
}
