package com.backend.farmon.apiPayload.exception.handler;

import com.backend.farmon.apiPayload.code.BaseErrorCode;
import com.backend.farmon.apiPayload.exception.GeneralException;

public class PortfolioHandler extends GeneralException {
    public PortfolioHandler(BaseErrorCode code) {
        super(code);
    }
}
