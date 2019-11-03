package by.danilov.cs.lab2.security.controller;

import by.danilov.cs.lab2.domain.Key;
import by.danilov.cs.lab2.domain.Token;
import by.danilov.cs.lab2.service.RSATokenService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth/token")
public class TokenController {

    private final RSATokenService tokenService;

    public TokenController(RSATokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping(
            value = "/request",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    private ResponseEntity<Token> requestToken(@RequestBody Key publicKey) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidKeySpecException, NoSuchPaddingException {
        System.out.println(publicKey.getKey());
        Token encryptedToken = tokenService.getEncryptedToken(publicKey.getKey());
        return ResponseEntity.ok(encryptedToken);
    }

}
