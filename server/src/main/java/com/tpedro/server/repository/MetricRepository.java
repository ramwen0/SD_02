package com.tpedro.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpedro.server.dto.AverageMetricsDTO;
import com.tpedro.server.dto.MetricDTO;
import com.tpedro.server.model.Metric;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
    // Consultar métricas por ID de dispositivo
    @Query("SELECT m FROM Metric m WHERE m.device.id = :deviceId AND m.timestamp BETWEEN :start AND :end ORDER BY m.timestamp DESC")
    List<Metric> findRawMetrics(@Param("deviceId") String deviceId, 
                                @Param("start") LocalDateTime start, 
                                @Param("end") LocalDateTime end);

    // Query para calcular média (usada no relatório)
    @Query("SELECT AVG(m.temperatura) FROM Metric m WHERE m.device.edificio = :edificio AND m.timestamp BETWEEN :start AND :end")
    Double getAverageTemperatureByEdificio(@Param("edificio") String edificio,
                                            @Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);

    // Média filtrando por Edifício
    @Query("SELECT new com.tpedro.server.dto.AverageMetricsDTO(AVG(m.temperatura), AVG(m.humidade))" + 
            "FROM Metric m JOIN m.device d " +
            "WHERE d.edificio = :id AND m.timestamp BETWEEN :start AND :end")
    AverageMetricsDTO findAverageByEdificio(@Param("id") String id, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Média filtrando por Sala
    @Query("SELECT new com.tpedro.server.dto.AverageMetricsDTO(AVG(m.temperatura), AVG(m.humidade))" + 
        "FROM Metric m JOIN m.device d " +
        "WHERE d.sala = :id AND m.timestamp BETWEEN :start AND :end")
    AverageMetricsDTO findAverageBySala(@Param("id") String id, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Média filtrando por Piso
    @Query("SELECT new com.tpedro.server.dto.AverageMetricsDTO(AVG(m.temperatura), AVG(m.humidade))" + 
        "FROM Metric m JOIN m.device d " +
        "WHERE d.piso = :id AND m.timestamp BETWEEN :start AND :end")
    AverageMetricsDTO findAverageByPiso(@Param("id") String id, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Média filtrando por Departamento
    @Query("SELECT new com.tpedro.server.dto.AverageMetricsDTO(AVG(m.temperatura), AVG(m.humidade))" + 
        "FROM Metric m JOIN m.device d " +
        "WHERE d.departamento = :id AND m.timestamp BETWEEN :start AND :end")
    AverageMetricsDTO findAverageByDepartamento(@Param("id") String id, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
