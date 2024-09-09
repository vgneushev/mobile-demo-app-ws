package com.devdemo.app.ws.ui.model.response.operation;

public enum RequestOperationStatus {
    ERROR(Boolean.FALSE),
    SUCCESS(Boolean.TRUE);

    final boolean result;
    RequestOperationStatus(final boolean result){
        this.result = result;
    }
}
