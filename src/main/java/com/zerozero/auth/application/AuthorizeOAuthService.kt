package com.zerozero.auth.application

import com.zerozero.auth.infrastructure.oauth.core.Provider
import org.springframework.stereotype.Service
import java.net.URI

@Service
class AuthorizeOAuthService(
    private val oAuthRestClientFactory: OAuthRestClientFactory
) {

    fun getAuthorizeUrl(providerName: String): URI {
        val provider = Provider.valueOf(providerName.uppercase())
        val oAuthRestClient = oAuthRestClientFactory.getOAuthRestClient(provider)
        return oAuthRestClient.authUrl
    }
}
