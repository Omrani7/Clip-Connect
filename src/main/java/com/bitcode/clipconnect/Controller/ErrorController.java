package com.bitcode.clipconnect.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        // Get the error status code
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // Create a response map with the error message
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Unexpected error occurred");
        response.put("status", status != null ? HttpStatus.valueOf(Integer.valueOf(status.toString())) : HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public String getErrorPath() {
        return "/error";
    }
}
