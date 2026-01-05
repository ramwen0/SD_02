package com.tpedro.mqtt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MqttClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MqttClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // lógica do simulador
        new MqttSensorSimulatorLogic().start();
    }
}

class MqttSensorSimulatorLogic {
    public void start() {
        org.eclipse.paho.client.mqttv3.MqttClient client = null;
        try {
            client = new org.eclipse.paho.client.mqttv3.MqttClient("tcp://broker.hivemq.com:1883", "sensor_sala_101_teste");
            client.connect();
            System.out.println("Simulador MQTT iniciado!");

            while (true) {
                String payload = "{\"id\":\"sensor_sala_101\", \"temperatura\":22.0, \"humidade\":50.0, \"timestamp\":\"2025-12-31T12:00:00\"}";
                client.publish("uevora/metrics", new org.eclipse.paho.client.mqttv3.MqttMessage(payload.getBytes()));
                System.out.println("Enviado: " + payload);
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null && client.isConnected()) {
                try {
                    client.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}