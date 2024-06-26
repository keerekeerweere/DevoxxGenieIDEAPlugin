package com.devoxx.genie.model.enumarations;

import lombok.Getter;

@Getter
public enum ModelProvider {
    Ollama("Ollama"),
    LMStudio("LMStudio"),
    GPT4All("GPT4All"),
    OpenAI("OpenAI"),
    Anthropic("Anthropic"),
    Mistral("Mistral"),
    Groq("Groq"),
    DeepInfra("DeepInfra");

    private final String name;

    ModelProvider(String name) {
        this.name = name;
    }

}
