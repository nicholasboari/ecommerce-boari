package com.ecommerceboari.api.dto.order;

import com.ecommerceboari.api.dto.ProductDTO;
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
public class OrderResponseDTO {

    private Long id;
    private Double total;
    private LocalDateTime moment;
    private List<ProductDTO> products = new ArrayList<>();
}
