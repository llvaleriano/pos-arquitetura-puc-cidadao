package br.gov.bomdestino.cidadao.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/servicos")
public class ServicosController {

    @GetMapping
    public String teste() {
        return "Serviços ao cidadao";
    }
}
