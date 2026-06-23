package com.example.productsales.controller;

import com.example.productsales.dto.TagRequest;
import com.example.productsales.dto.TagResponse;
import com.example.productsales.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService service;

    @PostMapping
    public ResponseEntity<TagResponse> save(@Valid @RequestBody TagRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getId(id));
    }

    @GetMapping("/search")
    public ResponseEntity<TagResponse> getByName(@RequestParam String name) {
        return ResponseEntity.ok(service.getByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> update(@PathVariable Long id,
                                              @Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(service.update(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
