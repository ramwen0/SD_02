package com.tpedro.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tpedro.server.repository.DeviceRepository;
import com.tpedro.server.repository.MetricRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SystemController {
    private final DeviceRepository deviceRepository;
    private final MetricRepository metricRepository;

    public SystemController(DeviceRepository deviceRepository, MetricRepository metricRepository) {
        this.deviceRepository = deviceRepository;
        this.metricRepository = metricRepository;
    }

    // Estatísticas
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalDevices = deviceRepository.count();
        long totalMetrics = metricRepository.count();

        // contagem por protocolo
        stats.put("totalDevices", totalDevices);
        stats.put("totalMetrics", totalMetrics);
        stats.put("activeDevices", deviceRepository.countByAtivoTrue());
        
        long inactiveDevices = totalDevices - deviceRepository.countByAtivoTrue();
        stats.put("inactiveDevices", inactiveDevices);

        return ResponseEntity.ok(stats);
    }
}
