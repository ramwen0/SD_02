package com.tpedro.rest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RestSensorSimulator implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    public static void main(String[] args) {
        SpringApplication.run(RestSensorSimulator.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String url = "http://localhost:8080/api/metrics/ingest";
        String deviceId = "sensor_sala_101";

        System.out.println("Simulador REST iniciado para: " + deviceId);

        double currentTemp = 20.0;
        double currentHum = 50.0;

        while (true) {
            // Dados sintéticos
            currentTemp += (random.nextDouble() - 0.5);
            currentHum += (random.nextDouble() - 0.5);
            
            // Criar payload JSON com um Map (não precisa da classe Metric do server)
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", deviceId);
            payload.put("temperatura", currentTemp);
            payload.put("humidade", currentHum);
            payload.put("timestamp", LocalDateTime.now().toString());

            try {
                // Enviar para o servidor
                restTemplate.postForObject(url, payload, String.class);
                System.out.println("Enviado via REST: " + currentTemp + "ºC");
            } catch (Exception e) {
                System.err.println("Erro ao contactar o servidor: " + e.getMessage());
            }

            Thread.sleep(5000); // Esperar 5 segundos
        }
    }
}