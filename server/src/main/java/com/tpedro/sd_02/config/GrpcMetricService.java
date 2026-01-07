package com.tpedro.sd_02.config;

import com.tpedro.sd_02.grpc.*;
import com.tpedro.sd_02.model.*;
import com.tpedro.sd_02.repository.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@GrpcService
public class GrpcMetricService extends MetricServiceGrpc.MetricServiceImplBase {
    private final DeviceRepository deviceRepository;
    private final MetricRepository metricRepository;

    public GrpcMetricService (DeviceRepository deviceRepository, MetricRepository metricRepository) {
        this.deviceRepository = deviceRepository;
        this.metricRepository = metricRepository;
    }

    @Override
    public void sendMetric(MetricRequest request, StreamObserver<MetricResponse> responseObserver) {
        System.out.println("Recebido pedido gRPC do dispositivo " + request.getDeviceId());

        Optional<Device> deviceOpt = deviceRepository.findById(request.getDeviceId());

        MetricResponse response;

        if (deviceOpt.isPresent()) { // Se o dispositivo existir, guardar métrica
            Metric m = new Metric();
            m.setDevice(deviceOpt.get());
            m.setTemperatura(request.getTemperatura());
            m.setHumidade(request.getHumidade());
            try { // Converter para LocalDateTime
                m.setTimestamp(LocalDateTime.parse(request.getTimestamp(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } catch (Exception e) {
                // Caso o formato falhe, usar hora do server
                m.setTimestamp(LocalDateTime.now());
            }
            
            metricRepository.save(m);

            response = MetricResponse.newBuilder()
                                    .setStatus("OK")
                                    .setMessage("Métrica guardada via gRPC!")
                                    .build();
        } else {
            response = MetricResponse.newBuilder()
                                    .setStatus("ERROR")
                                    .setMessage("Dispositivo não existe.")
                                    .build();
        }

        // Enviar resposta e fechar canar
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
