package com.ford.chibuisi.springcloudfunctiongcppubsub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentReport {
    private UUID uuid;
    private String user;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private String status;
}
