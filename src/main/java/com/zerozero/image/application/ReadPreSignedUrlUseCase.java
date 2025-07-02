package com.zerozero.image.application;

import com.zerozero.core.util.AWSS3Service;
import com.zerozero.image.domain.model.Image;
import com.zerozero.image.domain.model.ImagePrefix;
import com.zerozero.image.exception.ImageErrorType;
import com.zerozero.image.exception.ImageException;
import com.zerozero.image.presentation.request.PreSignedUrlRequest;
import com.zerozero.image.presentation.response.PreSignedUrlResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReadPreSignedUrlUseCase {

    private final AWSS3Service awss3Service;

    public PreSignedUrlResponse execute(PreSignedUrlRequest preSignedUrlRequest) {
        ImagePrefix imagePrefix = ImagePrefix.fromString(preSignedUrlRequest.prefix());
        if (imagePrefix == null) {
            log.error("[ReadPreSignedUrlResponse] Invalid imagePrefix: {}", preSignedUrlRequest.prefix());
            throw new ImageException(ImageErrorType.INVALID_IMAGE_PREFIX);
        }

        String fileName = preSignedUrlRequest.fileName();
        String extension = FilenameUtils.getExtension(fileName).toUpperCase();
        Image.validateExtension(extension);

        String preSignedUrl = awss3Service.getPreSignedUrl(imagePrefix.name().toLowerCase(), fileName);
        String objectUrl = awss3Service.getObjectUrlFromPreSignedUrl(preSignedUrl);
        log.info("[ReadPreSignedUrlResponse] Generated URLs - PreSigned: {}, Object: {}", preSignedUrl, objectUrl);
        return PreSignedUrlResponse.of(preSignedUrl, objectUrl);
    }

}
