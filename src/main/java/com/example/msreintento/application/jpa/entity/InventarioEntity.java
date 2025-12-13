package com.example.msreintento.application.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "INVENTARIO")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @Column(name = "cantidad")
    private Integer cantidad;
}


