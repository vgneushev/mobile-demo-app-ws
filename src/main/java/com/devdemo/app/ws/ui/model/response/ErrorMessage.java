package com.devdemo.app.ws.ui.model.response;

import lombok.NonNull;

import java.util.Date;

public record ErrorMessage (@NonNull Date timestamps, @NonNull String message) {}
