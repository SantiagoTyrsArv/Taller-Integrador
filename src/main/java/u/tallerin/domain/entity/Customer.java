package u.tallerin.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends Person {

    @Column(nullable = false)
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();

    public String getRole() {
        return "CUSTOMER";
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return;
        }
        boolean exists = vehicles.stream().anyMatch(existing ->
                Objects.equals(existing.getId(), vehicle.getId()));
        if (!exists) {
            vehicles.add(vehicle);
        }
        vehicle.setCustomer(this);
    }

    public void addOrder(Order order) {
        if (order == null) {
            return;
        }
        boolean exists = orders.stream().anyMatch(existing ->
                Objects.equals(existing.getId(), order.getId()));
        if (!exists) {
            orders.add(order);
        }
        order.setCustomer(this);
    }
}
