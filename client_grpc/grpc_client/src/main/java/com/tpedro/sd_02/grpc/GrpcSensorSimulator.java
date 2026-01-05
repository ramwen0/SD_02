package com.tpedro.sd_02.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Random;
import java.time.LocalDateTime;

public class GrpcSensorSimulator {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        // Criar stub do cliente
        MetricServiceGrpc.MetricServiceBlockingStub stub = MetricServiceGrpc.newBlockingStub(channel);

        String deviceId = "gateway_01";
        Random random = new Random();

        System.out.println("Simulador gRPC iniciado, a enviar para localhost:9090...");

        try {
            while(true) {
                // Gerar dados
                double temp = 15 + (random.nextDouble() * 15);
                double hum = 30 + (random.nextDouble() * 50);

                // Pedido gRPC
                MetricRequest request = MetricRequest.newBuilder()
                    .setDeviceId(deviceId)
                    .setTemperatura(temp)
                    .setHumidade(hum)
                    .setTimestamp(LocalDateTime.now().toString())
                    .build();

                // Enviar pedido e receber resposta
                MetricResponse response = stub.sendMetric(request);

                System.out.println("Enviado: " + temp + "ºC | Resposta: " + response.getStatus() + " - " + response.getMessage());

                // Aguardar 5 segundos antes do próximo envio
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            System.err.println("Erro no simulador gRPC: " + e.getMessage());
        } finally {
            channel.shutdown();
        }
    }
}

