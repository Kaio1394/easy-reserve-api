package br.com.easyreserve.exception;

import lombok.Getter;

@Getter
public class ModuleAlreadyExistsException extends RuntimeException {
    private final String slug;

    public ModuleAlreadyExistsException(String slug) {
        this.slug = slug;
    }
}
