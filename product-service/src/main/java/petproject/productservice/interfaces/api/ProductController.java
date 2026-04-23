package petproject.productservice.interfaces.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import petproject.productservice.application.command.CreateProductCommand;
import petproject.productservice.application.service.ProductService;
import petproject.productservice.infrastructure.security.AuthenticatedUser;
import petproject.productservice.interfaces.api.dto.CreateProductRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import petproject.productservice.interfaces.api.mapper.ProductMapper;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductMapper productMapper;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody @Valid CreateProductRequestDto createProductRequestDto
    ) {
        CreateProductCommand createProductCommand = productMapper.fromDtoToCommand(createProductRequestDto, user.userId());
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(createProductCommand));
    }
}
