package com.tpedro.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tpedro.server.dto.AverageMetricsDTO;
import com.tpedro.server.dto.MetricDTO;
import com.tpedro.server.model.*;
import com.tpedro.server.repository.*;

import java.time.LocalDateTime;
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

    @GetMapping("/average")
    public ResponseEntity<AverageMetricsDTO> getAverages(
        @RequestParam String level,
        @RequestParam String id,
        @RequestParam(required = false) String from, 
        @RequestParam(required = false) String to) {
            // Formatar ID
            String formattedId = id.replace("_", " ");
            // Adicionar datas
            LocalDateTime start = (from != null) ? LocalDateTime.parse(from) : LocalDateTime.now().minusDays(1);
            LocalDateTime end = (from != null) ? LocalDateTime.parse(to) : LocalDateTime.now().minusDays(1);

            AverageMetricsDTO result;

            // Escolher query baseada no nível
            switch (level.toLowerCase()) {
                case "edificio":
                    result = metricRepository.findAverageByEdificio(formattedId, start, end);
                    break;
                case "piso":
                    result = metricRepository.findAverageByPiso(formattedId, start, end);
                    break;
                case "departamento":
                    result = metricRepository.findAverageByDepartamento(formattedId, start, end);
                    break;
                case "sala":
                    result = metricRepository.findAverageByDepartamento(formattedId, start, end);
                    break;
                default:
                    return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(result);
        }
    
}
