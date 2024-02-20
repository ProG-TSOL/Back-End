package com.backend.prog.global.auth.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@FeignClient(name = "OAuth2OpenApi", url = "DYNAMIC_URI")
public interface OAuth2Api {

    @GetMapping
    Object getAccessToken(URI uri, @RequestHeader("Accept") String type, @RequestParam("client_id") String client_id, @RequestParam("client_secret") String client_secret, @RequestParam("code") String code);

    @GetMapping
    Object getUserInfo(URI uri, @RequestHeader("Authorization") String accessToken);

    @GetMapping
    List<Object> getEmailForGithub(URI uri, @RequestHeader("Authorization") String accessToken);
}
