package com.webflux.project.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("clients")
public class Client {
    @Id
    private Long id;
    private String name;
}
