package com.example.msreintento.application.service;

import com.example.msreintento.application.dto.PagoErrorEvent;
import com.example.msreintento.application.dto.ReintentoResponseDto;

public interface ReintentoService {

    ReintentoResponseDto reintentarPago(PagoErrorEvent pagoErrorEvent);
}
