package com.tpedro.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tpedro.server.controller.MetricController;
import com.tpedro.server.dto.MetricDTO;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfig {
    
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] {"tcp://broker.hivemq.com:1883"}); // Endereço do broker
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mtqqInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        // servidor subscreve-se como 'spring-server' e subscreve o tópico das métricas
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("spring-server", mqttClientFactory(), "uevora/metrics");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mtqqInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mtqqInputChannel")
    public MessageHandler handler(MetricController controller) {
        return message -> {
            String payload = (String) message.getPayload();
            try {
                // Converte Srting JSON para objeto MetricDTO
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule()); // Suporta DateTime
                MetricDTO dto = mapper.readValue(payload, MetricDTO.class);

                // Usar a mesma lógica do controller para salvar a métrica
                controller.ingestMetric(dto);
                System.out.println("Métrica recebida via MQTT: " + payload);
            } catch (Exception e) {
                System.err.println("Erro ao processar a métrica MQTT: " + e.getMessage());
            }
        };
    }
}
