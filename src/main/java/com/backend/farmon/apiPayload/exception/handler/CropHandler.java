package com.backend.farmon.apiPayload.exception.handler;

import com.backend.farmon.apiPayload.code.BaseErrorCode;
import com.backend.farmon.apiPayload.exception.GeneralException;

public class CropHandler extends GeneralException {
    public CropHandler(BaseErrorCode code) { super(code); }

}
