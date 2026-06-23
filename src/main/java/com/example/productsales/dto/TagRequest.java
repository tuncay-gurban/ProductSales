package com.example.test2.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagRequest {
    @NotBlank(message = "Ad bos ola bilmez")
    private String name;
}
