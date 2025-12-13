package com.example.msreintento.application.jpa.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "METODO_PAGO")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_pago")
    private Long id;

    @Column(name = "tipo", nullable = false, unique = true, length = 50)
    private String tipo;

}


