package com.tpedro.sd_02.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double temperatura;
    private Double humidade;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    public Metric() {
    }
    public Metric(Double temperatura, Double humidade, LocalDateTime timestamp, Device device) {
        this.temperatura = temperatura;
        this.humidade = humidade;
        this.timestamp = timestamp;
        this.device = device;
    }

    public String getId() {
        return id.toString();
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Double getTemperatura() {
        return temperatura;
    }
    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getHumidade() {
        return humidade;
    }
    public void setHumidade(Double humidade) {
        this.humidade = humidade;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Device getDevice() {
        return device;
    }
    public void setDevice(Device device) {
        this.device = device;
    }
}
