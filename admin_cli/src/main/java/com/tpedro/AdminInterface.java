package com.tpedro;

import org.springframework.web.client.RestTemplate;
import java.util.Scanner;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

public class AdminInterface {
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Sistema Distribuído de Monitorização Ambiental (Admin) ===");
            System.out.println("1. Gestão de Dispositivos");
            System.out.println("2. Consulta de Métricas");
            System.out.println("3. Estatísticas do Sistema");
            System.out.println("0. Sair");
            System.out.println("Opção: ");

            int op = Integer.parseInt(scanner.nextLine());

            switch (op) {
                case 1 -> menuDispositivos();
                case 2 -> menuMetricas();
                case 3 -> mostrarEstatisticas();
                case 0 -> System.exit(0);
                default -> System.out.println("Opção inválida.");
            }

        }
    }

    private static void menuDispositivos() {
        System.out.println("\n== Menu de dispositivos ==");
        System.out.println("1. Listar todos");
        System.out.println("2. Adicionar dispositivo");
        System.out.println("3. Atualizar dispositivo");
        System.out.println("4. Remover dispositivo");
        System.out.println("5. Ver detalhes de um dispositivo");
        System.out.println("Opção: ");

        int subOp = Integer.parseInt(scanner.nextLine());

        switch (subOp) {
            case 1:
                @SuppressWarnings("unchecked") List<Map<String,Object>> devices = restTemplate.getForObject(BASE_URL + "/devices", List.class);
                System.out.println("\nID | Sala | Departamento | Piso | Edifício | Protocolo | Estado (ativo)");
                System.out.println("-------------------------------------------------");
                for (Map<String, Object> d : devices) {
                    System.out.printf("%s | %s | %s | %s | %s | %s | %s \n", 
                        d.get("id"), d.get("sala"), d.get("departamento"), d.get("piso"), d.get("edificio"), d.get("protocol"), d.get("ativo"));
                }
                break;
            case 2: 
                System.out.println("Insira o ID: "); String newId = scanner.nextLine();
                System.out.println("Insira a Sala: "); String newSala = scanner.nextLine();
                System.out.println("Insira o Departamento: "); String newDept = scanner.nextLine();
                System.out.println("Insira o Piso: "); String newPiso = scanner.nextLine();
                System.out.println("Insira o Edifício: "); String newEdificio = scanner.nextLine();
                System.out.println("Insira o Protocolo: "); String newProt = scanner.nextLine();
                System.out.println("Insira o Estado (dispositivo ativo?): "); Boolean newEstado = Boolean.parseBoolean(scanner.nextLine());

                Map<String, Object> newDevice = new HashMap<>();
                newDevice.put("id", newId);
                newDevice.put("sala", newSala);
                newDevice.put("departamento", newDept);
                newDevice.put("piso", newPiso);
                newDevice.put("edificio", newEdificio);
                newDevice.put("protocol", newProt);
                newDevice.put("estado", newEstado);
                restTemplate.postForObject(BASE_URL + "/devices", newDevice, Map.class);
                System.out.println("Dispositivo adicionado.");
                break;
            case 3:
                System.out.println("Insira o ID: "); String updatedId = scanner.nextLine();
                System.out.println("Insira a Sala: "); String updatedSala = scanner.nextLine();
                System.out.println("Insira o Departamento: "); String updatedDept = scanner.nextLine();
                System.out.println("Insira o Piso: "); String updatedPiso = scanner.nextLine();
                System.out.println("Insira o Edifício: "); String updatedEdificio = scanner.nextLine();
                System.out.println("Insira o Protocolo: "); String updatedProt = scanner.nextLine();
                System.out.println("Insira o Estado (dispositivo ativo?): "); Boolean updatedEstado = Boolean.parseBoolean(scanner.nextLine());

                Map<String, Object> updatedDevice = new HashMap<>();
                updatedDevice.put("id", updatedId);
                updatedDevice.put("sala", updatedSala);
                updatedDevice.put("departamento", updatedDept);
                updatedDevice.put("piso", updatedPiso);
                updatedDevice.put("edificio", updatedEdificio);
                updatedDevice.put("protocol", updatedProt);
                updatedDevice.put("estado", updatedEstado);
                restTemplate.put(BASE_URL + "/devices/" + updatedId, updatedDevice);
                System.out.println("Dispositivo atualizado.");
                break;
            case 4:
                try {
                    System.out.println("Insira o ID: "); String removeId = scanner.nextLine();
                    restTemplate.delete(BASE_URL + "/devices/" + removeId);
                    System.out.print("Dispositivo " + removeId + " apagado.");
                } catch (Exception e) {
                    System.err.println("Erro a eliminar: " + e.getMessage());
                }
                break;  
            case 5:
                System.out.println("Insira o ID do dispositivo: ");
                String checkId = scanner.nextLine();

                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> device = restTemplate.getForObject(BASE_URL + "/devices/" + checkId, Map.class);

                    if (device != null) {
                        System.out.println("\n=== Detalhes do Dispositivo ===");
                        System.out.println("ID | Sala | Departamento | Piso | Edifício | Protocolo | Estado");
                        System.out.println("------------------------------------------------------------------");
                        
                        System.out.printf("%s | %s | %s | %s | %s | %s | %s \n", 
                            device.get("id"), 
                            device.get("sala"), 
                            device.get("departamento"), 
                            device.get("piso"), 
                            device.get("edificio"), 
                            device.get("protocol"),
                            (boolean)device.get("ativo") ? "Ativo" : "Inativo"
                        );
                    }
                } catch (Exception e) {
                    System.out.println("Erro: Dispositivo com ID '" + checkId + "' não encontrado.");
                }
                break;    
            default:
                System.out.println("Opção inválida.");
                break;
        }

    }

    private static void menuMetricas() {
        System.out.println("\n== Menu de Métricas ==");
        System.out.println("1. Métricas médias por nível");
        System.out.println("2. Métricas brutas de um dispositivo");
        System.out.println("Opção: ");

        int subOp = Integer.parseInt(scanner.nextLine());

        switch (subOp) {
            case 1:
                System.out.println("Indique o Nível (sala/departamento/piso/edificio): "); String level = scanner.nextLine();
                System.out.print("ID da entidade (qual o valor do nível específico): "); String entityId = scanner.nextLine();
                System.out.print("Data de Início (opcional, ex: 2024-12-01T00:00:00): "); String from = scanner.nextLine();
                System.out.print("Data de Fim (opcional, ex: 2024-12-01T00:00:00): "); String to = scanner.nextLine();
                
                String url = String.format("%s/metrics/average?level=%s&id=%s", BASE_URL, level, entityId);
                if (!from.isEmpty()) url += "&from=" + from;
                if (!to.isEmpty()) url += "&to=" + to;

                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> res = restTemplate.getForObject(url, Map.class);
                    if (res != null && res.get("avgTemperatura") != null && res.get("avgHumidade") != null) {
                        System.out.println("\n\t-- Resultados --");
                        System.out.printf("%-20s | %-15s\n", "Métrica", "Valor Médio");
                        System.out.println("------------------------------------------------------");
                        System.out.printf("%-20s | %.2f ºC\n", "Temperatura", res.get("avgTemperatura"));
                        System.out.printf("%-20s | %.2f %%\n", "Humidade", res.get("avgHumidade"));
                    } else {
                        System.out.println("Dados irrelevantes para consulta.");
                    }
                } catch (Exception e) {
                    System.err.print("Erro a consultar métricas: " + e.getMessage());
                }
                break;
            case 2:
                System.out.print("ID do dispositivo: "); 
                String deviceId = scanner.nextLine();

                System.out.print("Data de Início (opcional, ex: 2024-12-01T14:30:00): "); 
                String fromRaw = scanner.nextLine();

                System.out.print("Data de Fim (opcional, ex: 2024-12-01T18:30:00): "); 
                String toRaw = scanner.nextLine();

                String urlRaw = BASE_URL + "/metrics/raw?deviceId=" + deviceId;

                if (!fromRaw.isEmpty()) urlRaw += "&from=" + fromRaw;
                if (!toRaw.isEmpty()) urlRaw += "&to=" + toRaw;

                try {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> metrics = restTemplate.getForObject(urlRaw, List.class);
                    if (metrics != null && !metrics.isEmpty()) {
                        System.out.println("\n\t=== Histórico de Métricas: " + deviceId + " ===");
                        System.out.printf("%-25s | %-12s | %-10s\n", "Data/Hora (Timestamp)", "Temp (ºC)", "Hum (%)");
                        System.out.println("----------------------------------------------------------------------");
                        for (Map<String, Object> m : metrics) {
                            System.out.printf("%-25s | %-12.2f | %-10.2f\n", 
                                m.get("timestamp"),
                                m.get("temperatura"), 
                                m.get("humidade")
                            );
                        }
                        System.out.println("----------------------------------------------------------------------");
                        System.out.println("Total de registos encontrados: " + metrics.size());
                    } else {
                        System.out.println("\n[!] Não foram encontrados dados para este dispositivo no intervalo selecionado.");
                    }
                } catch (Exception e) {
                    System.err.println("\n[Erro] Falha ao consultar métricas: " + e.getMessage());
                }
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    private static void mostrarEstatisticas() {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> stats = restTemplate.getForObject(BASE_URL + "/stats", Map.class);
            
            System.out.println("\n========= ESTATÍSTICAS DO SISTEMA =========");
            System.out.println("-------------------------------------------");
            System.out.printf("Total de Dispositivos Registados: %d\n", stats.get("totalDevices"));
            System.out.printf("Total de Métricas em Base de Dados: %d\n", stats.get("totalMetrics"));
            System.out.printf("Dispositivos ativos: %d\n", stats.get("activeDevices"));
            System.out.printf("Dispositivos inativos: %d\n", stats.get("activeDevices"));
            System.out.println("-------------------------------------------");
            
            if ((Integer)stats.get("totalDevices") > 0) {
                double mediaPorSensor = (double)((Integer)stats.get("totalMetrics")) / ((Integer)stats.get("totalDevices"));
                System.out.printf("Média de métricas por dispositivo: %.2f\n", mediaPorSensor);
            }
        } catch (Exception e) {
            System.out.println("Erro ao obter estatísticas: " + e.getMessage());
        }
    }
}