package com.zerozero.configuration.feign.kakao;

import com.zerozero.core.support.error.CoreException;
import com.zerozero.core.support.error.GlobalErrorType;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class KakaoFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("[KakaoFeignErrorDecoder] Feign request failed: method={}, status={}", methodKey, response.status());
        return new CoreException(GlobalErrorType.KAKAO_SERVICE_UNAVAILABLE);
    }
}
