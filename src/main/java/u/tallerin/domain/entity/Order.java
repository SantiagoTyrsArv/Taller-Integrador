package u.tallerin.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.MapKeyColumn;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import u.tallerin.domain.enums.OrderStatus;
import u.tallerin.exception.BusinessException;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_services",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_service_prices", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "service_id")
    @Column(name = "applied_price", precision = 19, scale = 2, nullable = false)
    private Map<Long, BigDecimal> appliedPrices = new LinkedHashMap<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @JsonIgnore
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    @PrePersist
    public void prePersist() {
        if (date == null) {
            date = LocalDateTime.now();
        }
        if (orderStatus == null) {
            orderStatus = OrderStatus.REGISTERED;
        }
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus status) {
        updateStatus(status);
    }

    public void updateStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            throw new BusinessException("Order status is required");
        }
        if (orderStatus == null) {
            orderStatus = newStatus;
            return;
        }
        if (orderStatus == newStatus) {
            return;
        }
        if (newStatus == OrderStatus.CANCELLED) {
            if (orderStatus == OrderStatus.DELIVERED) {
                throw new BusinessException("Delivered orders cannot be cancelled");
            }
            orderStatus = newStatus;
            return;
        }
        switch (orderStatus) {
            case REGISTERED:
                if (newStatus != OrderStatus.IN_PROGRESS) {
                    throw new BusinessException("Valid transition: REGISTERED -> IN_PROGRESS");
                }
                break;
            case IN_PROGRESS:
                if (newStatus != OrderStatus.COMPLETED) {
                    throw new BusinessException("Valid transition: IN_PROGRESS -> COMPLETED");
                }
                break;
            case COMPLETED:
                if (newStatus != OrderStatus.DELIVERED) {
                    throw new BusinessException("Valid transition: COMPLETED -> DELIVERED");
                }
                break;
            case DELIVERED:
            case CANCELLED:
                throw new BusinessException("Order status cannot be changed once closed");
            default:
                throw new BusinessException("Unsupported order status transition");
        }
        orderStatus = newStatus;
    }

    public void addService(Service service) {
        if (service == null) {
            throw new BusinessException("Service is required");
        }
        if (vehicle == null) {
            throw new BusinessException("Vehicle must be assigned before adding services");
        }
        if (service.getId() == null) {
            throw new BusinessException("Service must be persisted before it can be added to an order");
        }
        boolean exists = services.stream().anyMatch(existing ->
                Objects.equals(existing.getId(), service.getId()));
        if (!exists) {
            services.add(service);
        }
        appliedPrices.put(service.getId(), service.calculatePrice(vehicle));
    }

    public BigDecimal calculateTotal() {
        return appliedPrices.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isFullyPaid() {
        return payment != null && orderStatus == OrderStatus.COMPLETED;
    }
}
