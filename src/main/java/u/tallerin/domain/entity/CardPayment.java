package u.tallerin.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import u.tallerin.domain.enums.PaymentMethod;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("CARD")
public class CardPayment extends Payment {

    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String customerName;

    @Column(name = "authorization_code", nullable = false)
    private String authorization;

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CARD;
    }

    @PrePersist
    public void prepare() {
        setPaymentMethod(PaymentMethod.CARD);
        if (authorization == null || authorization.isBlank()) {
            authorization = UUID.randomUUID().toString();
        }
    }
}
