package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin API", description = "API for administrative tasks and checks.")
public class AdminController {

    @GetMapping
    @Operation(summary = "Get Admin Information", description = "Returns basic information for admin validation.")
    @ApiResponse(responseCode = "200", description = "Admin check successful")
    public String getAdminInfo() {
        return "Admin check";
    }

}