package u.tallerin.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    public String getFullName() {
        String safeName = name == null ? "" : name;
        String safeLastName = lastName == null ? "" : lastName;
        return (safeName + " " + safeLastName).trim();
    }

    public String getNif() {
        return id == null ? null : String.valueOf(id);
    }
}
