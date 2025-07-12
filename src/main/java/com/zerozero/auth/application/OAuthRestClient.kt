package com.zerozero.auth.application

import com.zerozero.auth.infrastructure.oauth.core.OAuthAccessTokenResponse
import com.zerozero.auth.infrastructure.oauth.core.OAuthResourceResponse
import java.net.URI

interface OAuthRestClient {
    val authUrl: URI

    fun getAccessToken(authCode: String): OAuthAccessTokenResponse?

    fun getResource(accessToken: String): OAuthResourceResponse?
}
