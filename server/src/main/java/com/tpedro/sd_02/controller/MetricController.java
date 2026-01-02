package com.tpedro.sd_02.controller;

import com.tpedro.sd_02.dto.MetricDTO;
import com.tpedro.sd_02.model.*;
import com.tpedro.sd_02.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/metrics")
public class MetricController {
    final MetricRepository metricRepository;
    final DeviceRepository deviceRepository;

    public MetricController(MetricRepository metricRepository, DeviceRepository deviceRepository) {
        this.metricRepository = metricRepository;
        this.deviceRepository = deviceRepository;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingestMetric(@RequestBody MetricDTO dto) {
        Optional<Device> deviceOpt = deviceRepository.findById(dto.getId()); // Verificar se dispositivo existe
        if (deviceOpt.isEmpty()) { // Se não existir, descartar métrica
            System.out.println("Métrica descartada. Dispositivo " + dto.getId() + " não registado.");
            return ResponseEntity.badRequest().body("Erro: Dispositivo não encontrado.");
        }

        // Converter DTO para entidade Metric
        Device device = deviceOpt.get();
        Metric metric = new Metric();
        metric.setTemperatura(dto.getTemperatura());
        metric.setHumidade(dto.getHumidade());
        metric.setTimestamp(dto.getTimestamp());
        metric.setDevice(device); // Associar métricas ao dispositivo

        metricRepository.save(metric);

        return ResponseEntity.ok("Métrica processada com sucesso via REST.");
    }
}
