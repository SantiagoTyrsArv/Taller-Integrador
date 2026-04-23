package u.tallerin.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import u.tallerin.domain.enums.ServiceType;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "services")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "service_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal basePrice;

    @Getter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(name = "service_category", nullable = false)
    protected ServiceType serviceType;

    public abstract ServiceType getServiceType();

    public abstract BigDecimal calculatePrice(Vehicle vehicle);

    public String getName() {
        return name;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }
}
