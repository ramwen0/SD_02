package com.tpedro.sd_02.controller;

import com.tpedro.sd_02.model.Device;
import com.tpedro.sd_02.repository.DeviceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    // POST -> Registar novo dispositivo
    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        Device savedDevice = deviceRepository.save(device);
        return new ResponseEntity<>(savedDevice, HttpStatus.CREATED);
    }

    // GET -> Listar todos os dispositivos
    @GetMapping
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    // GET -> Obter dispositivo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable String id) {
        return deviceRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    // PUT -> Atualizar dispositivo
    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable String id, @RequestBody Device deviceDetails) {
        return deviceRepository.findById(id).map(device -> {
            device.setProtocol(deviceDetails.getProtocol());
            device.setSala(deviceDetails.getSala());
            device.setDepartamento(deviceDetails.getDepartamento());
            device.setPiso(deviceDetails.getPiso());
            device.setEdificio(deviceDetails.getEdificio());
            device.setAtivo(deviceDetails.isAtivo());
            Device updatedDevice = deviceRepository.save(device);
            return ResponseEntity.ok(updatedDevice);
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE -> Remover dispositivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String id) {
        if (deviceRepository.existsById(id)) {
            deviceRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
