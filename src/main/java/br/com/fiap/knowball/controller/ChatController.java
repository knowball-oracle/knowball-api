package br.com.fiap.knowball.controller;

import br.com.fiap.knowball.dto.ChatRequest;
import br.com.fiap.knowball.model.User;
import br.com.fiap.knowball.repository.UserRepository;
import br.com.fiap.knowball.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

@Tag(name = "Chat", description = "Assistente virtual Kiko — guia interativo do Knowball")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final UserRepository userRepository;

    @Operation(
            summary = "Conversar com o Kiko (streaming)",
            description = "Retorna a resposta em tempo real via Server-Sent Events."
    )
    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@Valid @RequestBody ChatRequest request, Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário autenticado não encontrado."));

        String userId = user.getId().toString();

        log.info(
                "POST /chat (stream) - userId={}, role={}",
                userId,
                user.getRole()
        );

        return chatService.sendMessage(
                        request.message(),
                        userId,
                        user.getRole()
                )
                .onErrorReturn(
                        "Desculpe, o Kiko está indisponível no momento. Tente novamente em instantes."
                );
    }
}