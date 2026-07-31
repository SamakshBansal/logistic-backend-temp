package com.logistics.auth.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryAgentResponse {

    private UUID id;
    private String name;
    private String email;
}