package br.com.fiap.respirabem.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Redireciona a raiz "/" para a documentação Swagger. */
@Hidden
@Controller
public class RaizController {

    @GetMapping("/")
    public String raiz() {
        return "redirect:/swagger-ui/index.html";
    }
}
