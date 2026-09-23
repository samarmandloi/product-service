package com.pm.productservice.controller;

import com.pm.productservice.dto.requestDto.CheckoutablePatchRequest;
import com.pm.productservice.dto.requestDto.CheckoutableRequest;
import com.pm.productservice.dto.responseDto.CheckoutableResponse;
import com.pm.productservice.service.CheckoutableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/checkoutables")
@RequiredArgsConstructor
public class CheckoutableController {

    private final CheckoutableService checkoutableService;

    @GetMapping
    public List<CheckoutableResponse> getAll() {
        return checkoutableService.getAll();
    }

    @GetMapping("/{id}")
    public CheckoutableResponse getById(
            @PathVariable UUID id) {

        return checkoutableService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CheckoutableResponse create(
            @Valid @RequestBody CheckoutableRequest request) {

        return checkoutableService.create(request);
    }

    @PatchMapping("/{id}")
    public CheckoutableResponse patch(
            @PathVariable UUID id,
            @RequestBody CheckoutablePatchRequest request) {

        return checkoutableService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id) {

        checkoutableService.delete(id);
    }
}