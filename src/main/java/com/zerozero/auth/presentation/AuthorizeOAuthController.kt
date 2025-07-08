package com.zerozero.auth.presentation

import com.zerozero.auth.application.AuthorizeOAuthUseCase
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView

@Controller
@RequestMapping("/oauth")
class AuthorizeOAuthController(
    private val authorizeOAuthUseCase: AuthorizeOAuthUseCase
) {

    @GetMapping("/{providerName}")
    fun authorizeOAuth(
        @PathVariable providerName: String
    ): RedirectView {
        val authUrl = authorizeOAuthUseCase.getAuthorizeUrl(providerName)
        return RedirectView(authUrl.toASCIIString())
    }
}

