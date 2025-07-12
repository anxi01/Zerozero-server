package com.zerozero.image.application

import com.zerozero.core.util.AWSS3Service
import com.zerozero.image.domain.model.Image
import com.zerozero.image.domain.model.ImagePrefix
import com.zerozero.image.exception.ImageErrorType
import com.zerozero.image.exception.ImageException
import com.zerozero.image.presentation.request.PreSignedUrlRequest
import com.zerozero.image.presentation.response.PreSignedUrlResponse
import org.apache.commons.io.FilenameUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class ReadPreSignedUrlUseCase(
    private val awsS3Service: AWSS3Service,
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun execute(preSignedUrlRequest: PreSignedUrlRequest): PreSignedUrlResponse {
        val imagePrefix = ImagePrefix.fromString(preSignedUrlRequest.prefix)
        if (imagePrefix == null) {
            log.error("[ReadPreSignedUrlResponse] Invalid imagePrefix: {}", preSignedUrlRequest.prefix)
            throw ImageException(ImageErrorType.INVALID_IMAGE_PREFIX)
        }

        val fileName = preSignedUrlRequest.fileName
        val extension = FilenameUtils.getExtension(fileName).uppercase(Locale.getDefault())
        Image.validateExtension(extension)

        val preSignedUrl = awsS3Service.getPreSignedUrl(imagePrefix.name.lowercase(), fileName)
        val objectUrl = awsS3Service.getObjectUrlFromPreSignedUrl(preSignedUrl)
        log.info("[ReadPreSignedUrlResponse] Generated URLs - PreSigned: {}, Object: {}", preSignedUrl, objectUrl)

        return PreSignedUrlResponse.of(preSignedUrl, objectUrl)
    }
}
