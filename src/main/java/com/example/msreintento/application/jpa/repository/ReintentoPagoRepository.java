package com.example.msreintento.application.jpa.repository;


import com.example.msreintento.application.jpa.entity.ReintentoPagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReintentoPagoRepository extends JpaRepository<ReintentoPagoEntity, Long> {
    Optional<ReintentoPagoEntity> findByPagoId(Long idPago);
}
