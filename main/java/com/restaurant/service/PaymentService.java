package com.restaurant.service;

import com.restaurant.dto.PaymentDTO;
import com.restaurant.entity.Order;
import com.restaurant.entity.Payment;
import com.restaurant.repository.OrderRepository;
import com.restaurant.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public PaymentDTO.PaymentResponse createPayment(PaymentDTO.CreatePaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(Payment.PaymentStatus.PENDING);

        Payment savedPayment = paymentRepository.save(payment);
        return new PaymentDTO.PaymentResponse(savedPayment);
    }

    @Transactional
    public PaymentDTO.PaymentResponse processPayment(Long paymentId, String transactionId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.markAsPaid(transactionId);
        Payment updatedPayment = paymentRepository.save(payment);

        // TODO: Обновляем статус заказа на "оплачен"
        // Order order = payment.getOrder();
        // order.setStatus(...);
        // orderRepository.save(order);

        return new PaymentDTO.PaymentResponse(updatedPayment);
    }

    public List<PaymentDTO.PaymentResponse> getPaymentsByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return paymentRepository.findByOrder(order).stream()
                .map(PaymentDTO.PaymentResponse::new)
                .collect(Collectors.toList());
    }

    public PaymentDTO.RevenueStatistics getRevenueStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal totalRevenue = paymentRepository.getTotalRevenueBetween(startDate, endDate);
        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }

        List<Object[]> revenueByMethod = paymentRepository.getRevenueByPaymentMethodBetween(startDate, endDate);
        Map<String, BigDecimal> revenueMap = revenueByMethod.stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> (BigDecimal) obj[1]
                ));

        // Теперь метод доступен
        Long totalTransactions = paymentRepository.countSuccessfulPaymentsBetween(startDate, endDate);

        return new PaymentDTO.RevenueStatistics(totalRevenue, revenueMap, totalTransactions, startDate, endDate);
    }

    public List<PaymentDTO.PaymentResponse> getPaymentsByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status).stream()
                .map(PaymentDTO.PaymentResponse::new)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO.PaymentResponse> getPaymentsByMethod(Payment.PaymentMethod method) {
        return paymentRepository.findByPaymentMethod(method).stream()
                .map(PaymentDTO.PaymentResponse::new)
                .collect(Collectors.toList());
    }
}