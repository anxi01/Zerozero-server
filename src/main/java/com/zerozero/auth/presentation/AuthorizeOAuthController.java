package com.zerozero.auth.presentation;

import com.zerozero.auth.application.AuthorizeOAuthUseCase;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class AuthorizeOAuthController {

  private final AuthorizeOAuthUseCase authorizeOAuthUseCase;

  @GetMapping("/{providerName}")
  public RedirectView authorizeOAuth(@PathVariable String providerName) {
    URI authUrl = authorizeOAuthUseCase.getAuthorizeUrl(providerName);
    return new RedirectView(authUrl.toASCIIString());
  }
}

