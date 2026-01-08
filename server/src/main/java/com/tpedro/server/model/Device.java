package com.tpedro.server.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Device {
    @Id
    private String id; 

    @Enumerated(EnumType.STRING)
    private ProtocolType protocolo;

    // Localização
    private String sala;
    private String departamento;
    private String piso;
    private String edificio;
    // Estado
    private boolean ativo;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL) // device é o 'dono' da relação
    private List<Metric> metrics;

    public Device() {
    }
    public Device(String id, ProtocolType protocolo, String sala, String departamento, String piso, String edificio, boolean ativo) {
        this.id = id;
        this.protocolo = protocolo;
        this.sala = sala;
        this.departamento = departamento;
        this.piso = piso;
        this.edificio = edificio;
        this.ativo = ativo;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getSala() {
        return sala;
    }
    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getPiso() {
        return piso;
    }
    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getEdificio() {
        return edificio;
    }
    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public ProtocolType getProtocol() {
        return protocolo;
    }
    public void setProtocol(ProtocolType protocolo) {
        this.protocolo = protocolo;
    }

    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
