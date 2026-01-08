package com.tpedro.server.dto;

import java.time.LocalDateTime;

public class MetricDTO {
    private String id; // Id do dispositivo que envia a métrica
    private Double temperatura;
    private Double humidade;
    private LocalDateTime timestamp;

    public String getId() {
        return id;
    }
    public void setId(String id) {
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
}