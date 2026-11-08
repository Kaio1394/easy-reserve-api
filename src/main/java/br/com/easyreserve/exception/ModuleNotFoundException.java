package br.com.easyreserve.exception;

import lombok.Getter;

@Getter
public class ModuleNotFoundException extends RuntimeException {
    private final String slug;

    public ModuleNotFoundException(String slug) {
        this.slug = slug;
    }
}
