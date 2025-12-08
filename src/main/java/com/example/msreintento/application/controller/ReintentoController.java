package com.example.msreintento.application.controller;


import com.example.msreintento.application.dto.PagoErrorEvent;
import com.example.msreintento.application.dto.ReintentoResponseDto;
import com.example.msreintento.application.service.ReintentoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reintento")
@RequiredArgsConstructor
public class ReintentoController {

    private final ReintentoService reintentoService;

    @PostMapping
    public ResponseEntity<ReintentoResponseDto> reintentar(@RequestBody PagoErrorEvent request) {
        ReintentoResponseDto reintentoResponseDto = reintentoService.reintentarPago(request);
        return ResponseEntity.ok(reintentoResponseDto);
    }

}
