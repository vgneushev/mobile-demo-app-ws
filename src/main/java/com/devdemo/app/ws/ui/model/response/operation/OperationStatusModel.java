package com.devdemo.app.ws.ui.model.response.operation;

import lombok.NonNull;

public record OperationStatusModel(@NonNull String operationResult, @NonNull String operationName) {}
