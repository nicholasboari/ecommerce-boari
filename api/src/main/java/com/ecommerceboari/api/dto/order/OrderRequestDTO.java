package com.ecommerceboari.api.dto.order;

import com.ecommerceboari.api.dto.ProductOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDTO {

    private Long id;
    private LocalDateTime moment;
    private List<ProductOrderDTO> products = new ArrayList<>();
}
