package com.nuvora.nuvora.order.entity;

public enum OrderStatus {
    PENDING,             // Aguardando pagamento
    PAID,                // Pagamento confirmado
    PROCESSING,          // Pedido em preparação
    SHIPPED,             // Pedido enviado
    DELIVERED,           // Pedido entregue
    CANCELED,            // Pedido cancelado
    PAYMENT_FAILED       // Pagamento recusado ou falhou
}
