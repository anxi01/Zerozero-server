package com.zerozero.core.util

import com.amazonaws.HttpMethod
import com.amazonaws.SdkClientException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.Headers
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.zerozero.image.exception.ImageErrorType
import com.zerozero.image.exception.ImageException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.function.Consumer

@Service
class AWSS3Service(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    private val amazonS3: AmazonS3
) {

    fun getPreSignedUrl(prefix: String, fileName: String): String {
        val filePath = createFilePath(prefix, fileName)
        val generatePreSignedUrlRequest = createGeneratePreSignedUrlRequest(bucket, filePath)
        try {
            val url = amazonS3.generatePresignedUrl(generatePreSignedUrlRequest)
            return url.toString()
        } catch (e: SdkClientException) {
            throw ImageException(ImageErrorType.FAILED_TO_MAKE_URL)
        }
    }

    fun getObjectUrlFromPreSignedUrl(preSignedUrl: String): String {
        try {
            val url = URL(preSignedUrl)
            return "${url.protocol}://${url.host}${url.path}"
        } catch (e: MalformedURLException) {
            throw ImageException(ImageErrorType.FAILED_TO_MAKE_URL)
        }
    }

    fun uploadImage(prefix: String, multipartFile: MultipartFile): String {
        val fileName = createFilePath(prefix, multipartFile.originalFilename)

        val objectMetadata = ObjectMetadata()
        objectMetadata.contentLength = multipartFile.size
        objectMetadata.contentType = multipartFile.contentType

        amazonS3.putObject(bucket, fileName, multipartFile.inputStream, objectMetadata)
        return getUrl(bucket, fileName)
    }

    fun uploadImages(prefix: String, multipartFiles: List<MultipartFile>): List<String> {
        val imageUrls: MutableList<String> = ArrayList()

        multipartFiles.forEach(Consumer { file ->
            val fileName = createFilePath(prefix, file.originalFilename)
            val objectMetadata = ObjectMetadata()
            objectMetadata.contentLength = file.size
            objectMetadata.contentType = file.contentType

            try {
                file.inputStream.use { inputStream ->
                    amazonS3.putObject(
                        PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
                    )
                }
            } catch (e: IOException) {
                throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.")
            }
            imageUrls.add(getUrl(bucket, fileName))
        })
        return imageUrls
    }

    private fun createGeneratePreSignedUrlRequest(bucket: String?, filePath: String): GeneratePresignedUrlRequest {
        val generatePreSignedUrlRequest = GeneratePresignedUrlRequest(bucket, filePath)
            .withMethod(HttpMethod.PUT)
            .withExpiration(preSignedUrlExpiration())
        generatePreSignedUrlRequest.addRequestParameter(
            Headers.S3_CANNED_ACL,
            CannedAccessControlList.PublicRead.toString()
        )
        return generatePreSignedUrlRequest
    }

    private fun preSignedUrlExpiration(): Date {
        val expiration = Date()
        var expTimeMillis = expiration.time
        expTimeMillis += (1000 * 60).toLong()
        expiration.time = expTimeMillis
        return expiration
    }

    private fun createFileUuid(): String {
        return UUID.randomUUID().toString()
    }

    private fun createFilePath(prefix: String, fileName: String?): String {
        val safeFileName = fileName?.takeIf { it.isNotBlank() } ?: "file"
        val fileUuid = createFileUuid()
        return "$prefix/$fileUuid-$safeFileName"
    }


    private fun getUrl(bucket: String?, fileName: String): String {
        return amazonS3.getUrl(bucket, fileName).toString()
    }
}
