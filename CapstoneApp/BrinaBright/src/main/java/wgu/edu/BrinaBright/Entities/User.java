package wgu.edu.BrinaBright.Entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String passwordHash;
    private String role;

    @OneToMany(mappedBy = "id")
    private List<UserBill> bills;

    @ManyToOne
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;


}