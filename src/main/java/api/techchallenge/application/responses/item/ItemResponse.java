package api.techchallenge.application.responses.item;

import lombok.Data;

import java.util.UUID;

@Data
public class ItemResponse {
    private UUID id;
    private String observacoes;
    private int quantidade;
}