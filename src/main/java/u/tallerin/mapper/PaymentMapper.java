package u.tallerin.mapper;

import java.util.UUID;
import org.mapstruct.Mapper;
import u.tallerin.domain.entity.CardPayment;
import u.tallerin.domain.entity.CashPayment;
import u.tallerin.domain.entity.Order;
import u.tallerin.domain.entity.Payment;
import u.tallerin.domain.enums.PaymentMethod;
import u.tallerin.dto.request.PaymentRequest;
import u.tallerin.dto.response.PaymentResponse;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    default Payment toEntity(PaymentRequest request) {
        if (request == null) {
            return null;
        }
        Payment payment;
        if (request.getPaymentMethod() == PaymentMethod.CASH) {
            CashPayment cashPayment = new CashPayment();
            cashPayment.setAmountPaid(request.getAmountPaid());
            cashPayment.setChange(null);
            payment = cashPayment;
        } else {
            CardPayment cardPayment = new CardPayment();
            cardPayment.setCardNumber(request.getCardNumber());
            cardPayment.setCustomerName(request.getCustomerName());
            cardPayment.setAuthorization(request.getAuthorization() == null || request.getAuthorization().isBlank()
                    ? UUID.randomUUID().toString()
                    : request.getAuthorization());
            payment = cardPayment;
        }
        Order order = new Order();
        order.setId(request.getOrderId());
        payment.setOrder(order);
        payment.setPaymentMethod(request.getPaymentMethod());
        return payment;
    }

    default PaymentResponse toResponse(Payment payment) {
        if (payment == null) {
            return null;
        }
        PaymentResponse.PaymentResponseBuilder builder = PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrder() == null ? null : payment.getOrder().getId())
                .amount(payment.getAmount())
                .date(payment.getDate())
                .paymentMethod(payment.getPaymentMethod());
        if (payment instanceof CashPayment cashPayment) {
            builder.amountPaid(cashPayment.getAmountPaid())
                    .change(cashPayment.getChange());
        }
        if (payment instanceof CardPayment cardPayment) {
            builder.cardNumber(cardPayment.getCardNumber())
                    .customerName(cardPayment.getCustomerName())
                    .authorization(cardPayment.getAuthorization());
        }
        return builder.build();
    }
}
