package u.tallerin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import u.tallerin.domain.entity.CardPayment;
import u.tallerin.domain.entity.CashPayment;
import u.tallerin.domain.entity.Order;
import u.tallerin.domain.entity.Payment;
import u.tallerin.domain.enums.PaymentMethod;
import u.tallerin.exception.BusinessException;
import u.tallerin.exception.ResourceNotFoundException;
import u.tallerin.repository.PaymentRepository;
import u.tallerin.service.OrderService;
import u.tallerin.service.PaymentService;

import java.math.BigDecimal;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    @Override
    public Payment registerPayment(Payment payment) {
        if (!validatePayment(payment)) {
            throw new BusinessException("Payment cannot be registered for this order");
        }

        Order order = orderService.getById(payment.getOrder().getId());
        BigDecimal total = order.calculateTotal();
        PaymentMethod method = payment.getPaymentMethod();

        if (method == PaymentMethod.CASH && payment instanceof CashPayment cashPayment) {
            BigDecimal amountPaid = cashPayment.getAmountPaid() == null ? total : cashPayment.getAmountPaid();
            if (amountPaid.compareTo(total) < 0) {
                throw new BusinessException("Cash amount paid is insufficient");
            }
            cashPayment.setAmountPaid(amountPaid);
            cashPayment.setChange(amountPaid.subtract(total));
            cashPayment.setAmount(total);
        } else if (method == PaymentMethod.CARD && payment instanceof CardPayment cardPayment) {
            if (!StringUtils.hasText(cardPayment.getCardNumber()) || !StringUtils.hasText(cardPayment.getCustomerName())) {
                throw new BusinessException("Card number and customer name are required for card payments");
            }
            cardPayment.setAmount(total);
        } else {
            throw new BusinessException("Unsupported payment type: " + method);
        }

        payment.setOrder(order);
        order.setPayment(payment);
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order " + orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> listAll() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validatePayment(Payment payment) {
        if (payment == null || payment.getOrder() == null || payment.getOrder().getId() == null) {
            return false;
        }
        Order order = orderService.getById(payment.getOrder().getId());
        return paymentRepository.findByOrderId(order.getId()).isEmpty();
    }
}
