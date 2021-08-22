package ec.telconet.persona.dto;

import lombok.Data;

import java.util.List;

@Data
public class Metricas {
    private String name;
    private List<Mediciones> measurements;

    @Data
    public static class Mediciones {
        private String statistic;
        private long value;
    }
}