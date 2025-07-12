package com.zerozero.auth.presentation

import com.zerozero.auth.application.AuthorizeOAuthService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView

@Controller
@RequestMapping("/oauth")
class AuthorizeOAuthController(
    private val authorizeOAuthService: AuthorizeOAuthService
) {

    @GetMapping("/{providerName}")
    fun authorizeOAuth(
        @PathVariable providerName: String
    ): RedirectView {
        val authUrl = authorizeOAuthService.getAuthorizeUrl(providerName)
        return RedirectView(authUrl.toASCIIString())
    }
}

