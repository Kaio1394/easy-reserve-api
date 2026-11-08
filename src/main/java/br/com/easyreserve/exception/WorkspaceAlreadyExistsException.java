package br.com.easyreserve.exception;

import lombok.Getter;

@Getter
public class WorkspaceAlreadyExistsException extends RuntimeException {
    private final String slug;

    public WorkspaceAlreadyExistsException(String slug) {
        this.slug = slug;
    }
}
