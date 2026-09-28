package com.rvrjc.colorido.controller;

import com.rvrjc.colorido.dto.AiChatRequest;
import com.rvrjc.colorido.dto.AiChatResponse;
import com.rvrjc.colorido.service.AiAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    @Autowired
    private AiAssistantService aiAssistantService;

    @PostMapping("/chat")
    public ResponseEntity<AiChatResponse> chat(@RequestBody AiChatRequest request) {
        String msg = (request != null) ? request.getMessage() : "";
        AiChatResponse response = aiAssistantService.processQuestion(msg);
        return ResponseEntity.ok(response);
    }
}
