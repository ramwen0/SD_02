package com.tpedro.sd_02.repository;

import com.tpedro.sd_02.dto.AverageMetricsDTO;
import com.tpedro.sd_02.model.Metric;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
    // Consultar métricas por ID de dispositivo
    List<Metric> findByDeviceIdAndTimestampBetween(String deviceId, LocalDateTime start, LocalDateTime end);

    // Query para calcular média (usada no relatório)
    @Query("SELECT AVG(m.temperatura) FROM Metric m WHERE m.device.edificio = :edificio AND m.timestamp BETWEEN :start AND :end")
    Double getAverageTemperatureByEdificio(@Param("edificio") String edificio,
                                            @Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);

    // Média filtrando por Edifício
    @Query("SELECT new com.tpedro.sd_02.dto.AverageMetricsDTO(AVG(m.temperatura), AVG(m.humidade))" + 
            "FROM Metric m JOIN m.device d " +
            "WHERE d.edificio = :id AND m.timestamp BETWEEN :start AND :end")
    AverageMetricsDTO findAverageByEdificio(@Param("id") String id, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Média filtrando por Sala
    @Query("SELECT new com.tpedro.sd_02.dto.AverageMetricsDTO(AVG(m.temperatura), AVG(m.humidade))" + 
        "FROM Metric m JOIN m.device d " +
        "WHERE d.sala = :id AND m.timestamp BETWEEN :start AND :end")
    AverageMetricsDTO findAverageBySala(@Param("id") String id, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Média filtrando por Piso
    @Query("SELECT new com.tpedro.sd_02.dto.AverageMetricsDTO(AVG(m.temperatura), AVG(m.humidade))" + 
        "FROM Metric m JOIN m.device d " +
        "WHERE d.piso = :id AND m.timestamp BETWEEN :start AND :end")
    AverageMetricsDTO findAverageByPiso(@Param("id") String id, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Média filtrando por Departamento
    @Query("SELECT new com.tpedro.sd_02.dto.AverageMetricsDTO(AVG(m.temperatura), AVG(m.humidade))" + 
        "FROM Metric m JOIN m.device d " +
        "WHERE d.departamento = :id AND m.timestamp BETWEEN :start AND :end")
    AverageMetricsDTO findAverageByDepartamento(@Param("id") String id, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
