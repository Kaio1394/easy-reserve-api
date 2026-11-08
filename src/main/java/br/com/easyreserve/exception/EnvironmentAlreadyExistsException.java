package br.com.easyreserve.exception;

import lombok.Getter;

@Getter
public class EnvironmentAlreadyExistsException extends RuntimeException {
    private final String workspaceName;
    private final String environmentName;

    public EnvironmentAlreadyExistsException(String workspaceName, String environmentName) {
        this.workspaceName = workspaceName;
        this.environmentName = environmentName;
    }
}
