package by.danilov.cs.lab2.controller;

import by.danilov.cs.lab2.domain.FileName;
import by.danilov.cs.lab2.domain.Response;
import by.danilov.cs.lab2.domain.TextResponse;
import by.danilov.cs.lab2.security.store.KeyStore;
import by.danilov.cs.lab2.service.AESService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;

@RestController
@RequestMapping("/api/get/file")
public class FileProvidingController {

    private static final String FILE_PATH_PATTERN = "src\\main\\resources\\store\\files\\{0}.txt";

    private final AESService aesService;

    private final KeyStore keyStore;

    public FileProvidingController(AESService aesService, KeyStore keyStore) {
        this.aesService = aesService;
        this.keyStore = keyStore;
    }

    @PostMapping
    public ResponseEntity getText(@RequestBody FileName fileName) {

        try {
            String decryptedFilename = decrypt(fileName.getFileName());

            String text = readFile(decryptedFilename);
            String encryptedText = encrypt(text);

            TextResponse textResponse = new TextResponse(encryptedText);

            return ResponseEntity.ok(textResponse);

        } catch (IOException e) {
            Response notFoundResponse = Response.builder()
                    .message("File was not found")
                    .code(HttpStatus.NOT_FOUND.value())
                    .build();

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(notFoundResponse);

        } catch (Exception e) {
            Response badRequestResponse = Response.builder()
                    .message("400 Bad Request")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();

            return ResponseEntity
                    .badRequest()
                    .body(badRequestResponse);

        }
    }

    public static String readFile(String filename) throws IOException {

        String fullPath = MessageFormat.format(FILE_PATH_PATTERN, filename);

        return Files.readString(Paths.get(fullPath));

    }

    private String encrypt(String text) throws Exception {

        String key = keyStore.getToken();
        String iv = keyStore.getIv();

        return aesService.encryptAES_CBC(text, key, iv);
    }

    private String decrypt(String cipherText) throws Exception {

        String key = keyStore.getToken();
        String iv = keyStore.getIv();

        return aesService.decryptAES_CBC(cipherText, key, iv);
    }

}
