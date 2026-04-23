package u.tallerin.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import u.tallerin.domain.enums.PaymentMethod;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("CASH")
public class CashPayment extends Payment {

    @Column(precision = 19, scale = 2)
    private BigDecimal amountPaid;

    @Column(precision = 19, scale = 2)
    private BigDecimal change;

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CASH;
    }

    @PrePersist
    public void prepare() {
        setPaymentMethod(PaymentMethod.CASH);
    }
}
