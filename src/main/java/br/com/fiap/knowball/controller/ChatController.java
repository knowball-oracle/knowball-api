package br.com.fiap.knowball.controller;

import br.com.fiap.knowball.dto.ChatRequest;
import br.com.fiap.knowball.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Tag(name = "Chat", description = "Assistente virtual Kiko — guia interativo do Knowball")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @Operation(
            summary = "Conversar com o Kiko (streaming)",
            description = "Retorna a resposta em tempo real via Server-Sent Events."
    )
    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@Valid @RequestBody ChatRequest request, Authentication authentication) {
        String userId = authentication.getName();
        log.info("POST /chat (stream) - user={}", userId);

        return chatService.sendMessage(request.message(), userId)
                .onErrorReturn("Desculpe, o Kiko está indisponível no momento. Tente novamente em instantes.");
    }
}