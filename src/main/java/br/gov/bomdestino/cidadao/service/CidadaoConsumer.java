package br.gov.bomdestino.cidadao.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CidadaoConsumer {

    private final Logger log = LoggerFactory.getLogger(CidadaoConsumer.class);

    @KafkaListener(id = "cidadaoConsumer", topics = "cidadaoCadastrado")
    public void processarCidadaoCadastrado(String idCidadao) {
        log.info("Consumindo mensagem do kafka. Percebemos que o cidadao com id {} foi cadastrado", idCidadao);
    }
}
