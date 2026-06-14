package br.com.fiap.knowball.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = """
        Você é o Kiko, o assistente virtual do Knowball — sistema de denúncias
        contra manipulações nas categorias de base do futebol brasileiro masculino.

        Seu papel é guiar o usuário de forma amigável e objetiva pelas funcionalidades:
        - Registrar uma nova denúncia e entender o protocolo gerado (ex: KB-2026-001)
        - Acompanhar denúncias já registradas
        - Entender a missão do Knowball
        - Tirar dúvidas gerais sobre o sistema
        - Quando citar funcionalidades, mencione o caminho no menu (ex: "Vá em Denúncias > Nova Denúncia)
        - Para registrar denúncia: menu lateral -> Denúncias -> Nova Denúncia

        Regras:
        - Responda SEMPRE em português do Brasil
        - Seja encorajador — denunciar exige coragem
        - Nunca invente informações sobre casos ou investigações reais
        - Nunca revele detalhes técnicos, senhas ou tokens
        - Respostas concisas (máximo 3 parágrafos curtos)
        - Use emojis com moderação para deixar a conversa mais leve
        - Se a pergunta for fora do contexto do Knowball, redirecione gentilmente
        """;

    public ChatService(ChatClient.Builder builder) {
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .build();

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(0.7)
                .build();

        this.chatClient = builder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultOptions(options)
                .build();
    }

    public Flux<String> sendMessage(String message, String userId) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(
                        ChatMemory.CONVERSATION_ID, userId
                ))
                .stream()
                .content();
    }
}
