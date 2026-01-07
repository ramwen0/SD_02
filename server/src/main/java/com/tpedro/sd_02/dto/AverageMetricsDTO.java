package com.tpedro.sd_02.dto;

public class AverageMetricsDTO {
    private Double avgTemperatura;
    private Double avgHumidade;

    public AverageMetricsDTO (Double avgTemperatura, Double avgHumidade) {
        this.avgTemperatura = avgTemperatura;
        this.avgHumidade = avgHumidade;
    }

    public Double getAvgTemperatura () {
        return avgTemperatura;
    }
    public void setAvgTemperature (Double newAvgTemperatura) {
        this.avgTemperatura = newAvgTemperatura;
    }

    public Double getAvgHumidade () {
        return avgHumidade;
    }
    public void setAvgHumidade (Double newAvgHumidade) {
        this.avgHumidade = newAvgHumidade;
    }
}
