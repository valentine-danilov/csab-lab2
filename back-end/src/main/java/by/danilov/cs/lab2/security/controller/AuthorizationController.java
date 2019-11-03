package by.danilov.cs.lab2.security.controller;

import by.danilov.cs.lab2.domain.Response;
import by.danilov.cs.lab2.exception.BadRequestException;
import by.danilov.cs.lab2.security.details.UserDetails;
import by.danilov.cs.lab2.security.provider.AuthenticationProvider;
import by.danilov.cs.lab2.security.store.KeyStore;
import by.danilov.cs.lab2.service.AESService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {

    private final AESService aesService;
    private final KeyStore keyStore;
    private final AuthenticationProvider authProvider;

    public AuthorizationController(AESService aesService,
                                   KeyStore keyStore,
                                   AuthenticationProvider authProvider) {
        this.aesService = aesService;
        this.keyStore = keyStore;
        this.authProvider = authProvider;
    }

    @PostMapping
    public ResponseEntity<Response> authorize(@RequestBody UserDetails encryptedDetails) {
        try {
            UserDetails decryptedDetails = decryptDetails(encryptedDetails);
            boolean authenticationResult = authProvider.authorize(decryptedDetails);

            return getResponse(authenticationResult);

        } catch (BadRequestException e) {
            Response badRequest = Response.builder()
                    .message("400 Bad Request")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();

            return ResponseEntity.badRequest().body(badRequest);
        }
    }

    private ResponseEntity<Response> getResponse(boolean authenticationResult) {
        if (authenticationResult) {
            Response responseOK = Response.builder()
                    .message("Authentication successful")
                    .code(HttpStatus.OK.value())
                    .build();

            return ResponseEntity.ok(responseOK);
        } else {
            Response responseUnauthorized = Response.builder()
                    .message("Invalid login or password")
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .build();

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(responseUnauthorized);
        }
    }

    private UserDetails decryptDetails(UserDetails encryptedDetails) throws BadRequestException {

        try {
            String key = keyStore.getToken();
            String iv = keyStore.getIv();
            String decryptedLogin = aesService.decryptAES_CBC(encryptedDetails.getLogin(), key, iv);
            String decryptedPassword = aesService.decryptAES_CBC(encryptedDetails.getPassword(), key, iv);

            return new UserDetails(decryptedLogin, decryptedPassword);
        } catch (Exception e) {
            throw new BadRequestException();
        }

    }
}
