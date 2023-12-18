package com.example.maschinefactory.responses;

import java.util.Date;

public record ApiResponse(String message, Boolean success, Date timestamp) {}