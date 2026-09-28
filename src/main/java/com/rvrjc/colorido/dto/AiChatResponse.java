package com.rvrjc.colorido.dto;

public class AiChatResponse {
    private String response;
    private boolean inScope;

    public AiChatResponse() {}
    public AiChatResponse(String response, boolean inScope) {
        this.response = response;
        this.inScope = inScope;
    }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public boolean isInScope() { return inScope; }
    public void setInScope(boolean inScope) { this.inScope = inScope; }
}
