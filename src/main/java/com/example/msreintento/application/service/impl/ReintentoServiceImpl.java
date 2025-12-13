package com.example.msreintento.application.service.impl;

import com.example.msreintento.application.dto.PagoDto;
import com.example.msreintento.application.dto.PagoErrorEvent;
import com.example.msreintento.application.dto.ReintentoResponseDto;
import com.example.msreintento.application.jpa.entity.PagoEntity;
import com.example.msreintento.application.jpa.entity.ReintentoPagoEntity;
import com.example.msreintento.application.jpa.repository.ReintentoPagoRepository;
import com.example.msreintento.application.kafka.producer.PagoPendienteProducer;
import com.example.msreintento.application.service.ReintentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReintentoServiceImpl implements ReintentoService {

    private final PagoPendienteProducer pagoPendienteProducer;
    private final ReintentoPagoRepository reintentoPagoRepository;

    private static final int MAX_REINTENTOS = 3;

    @Override
    public ReintentoResponseDto reintentarPago(PagoErrorEvent pagoErrorEvent) {

        Optional<ReintentoPagoEntity> opt =
                reintentoPagoRepository.findByPagoId(pagoErrorEvent.getIdPago());

        if (opt.isPresent()) {

            ReintentoPagoEntity actual = opt.get();

            if (actual.getIntentoNumero() >= MAX_REINTENTOS) {
                log.warn("[ms-reintento] Pago {} alcanzó el límite de reintentos", pagoErrorEvent.getIdPago());

                return ReintentoResponseDto.builder()
                        .idPago(actual.getPago().getId())
                        .intentoActual(actual.getIntentoNumero())
                        .maxIntentos(MAX_REINTENTOS)
                        .estado("AGOTADO")
                        .mensaje("Se alcanzó el máximo de reintentos para este pago")
                        .build();
            }

            int siguienteIntento = actual.getIntentoNumero() + 1;

            actual.setIntentoNumero(siguienteIntento);
            actual.setEstadoReintento("REINTENTANDO");
            actual.setFechaReintento(LocalDateTime.now());
            actual.setMensajeError(pagoErrorEvent.getMotivoError());

            reintentoPagoRepository.save(actual);

            pagoPendienteProducer.enviar(PagoDto.builder()
                            .idOrden(pagoErrorEvent.getIdOrden())
                            .reprocesado(true)
                    .build()

            );

            return ReintentoResponseDto.builder()
                    .idPago(actual.getPago().getId())
                    .intentoActual(siguienteIntento)
                    .maxIntentos(MAX_REINTENTOS)
                    .estado("REINTENTANDO")
                    .mensaje("Reintento enviado al topic pago-pendiente")
                    .build();
        }

        ReintentoPagoEntity nuevo = ReintentoPagoEntity.builder()
                .pago(PagoEntity.builder()
                        .id(pagoErrorEvent.getIdPago())
                        .build())
                .intentoNumero(1)
                .estadoReintento("REINTENTANDO")
                .fechaReintento(LocalDateTime.now())
                .mensajeError(pagoErrorEvent.getMotivoError())
                .build();

        reintentoPagoRepository.save(nuevo);

        pagoPendienteProducer.enviar(
                PagoDto.builder()
                        .idOrden(pagoErrorEvent.getIdOrden())
                        .reprocesado(true)
                        .build()
        );

        return ReintentoResponseDto.builder()
                .idPago(pagoErrorEvent.getIdPago())
                .intentoActual(1)
                .maxIntentos(MAX_REINTENTOS)
                .estado("PRIMER_REINTENTO")
                .mensaje("Primer reintento registrado y enviado")
                .build();
    }
}
