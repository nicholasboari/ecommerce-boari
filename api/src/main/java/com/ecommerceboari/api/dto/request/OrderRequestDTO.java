package com.ecommerceboari.api.dto.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ecommerceboari.api.dto.ProductOrderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDTO {

    private Long id;
    private LocalDateTime moment;
    private List<ProductOrderDTO> products = new ArrayList<>();
}
