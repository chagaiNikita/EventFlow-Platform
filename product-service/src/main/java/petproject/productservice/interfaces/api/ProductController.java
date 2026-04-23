package petproject.productservice.interfaces.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import petproject.productservice.application.command.CreateProductCommand;
import petproject.productservice.application.service.ProductService;
import petproject.productservice.domain.model.UserId;
import petproject.productservice.infrastructure.security.AuthenticatedUser;
import petproject.productservice.interfaces.api.dto.CreateProductRequestDto;
import org.springframework.http.ResponseEntity;
import petproject.productservice.interfaces.api.dto.ProductResponseDto;
import petproject.productservice.interfaces.api.mapper.ProductMapper;

import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductMapper productMapper;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody @Valid CreateProductRequestDto createProductRequestDto
    ) {
        CreateProductCommand createProductCommand = productMapper.fromDtoToCommand(createProductRequestDto, user.userId());
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toProductResponseDto(productService.createProduct(createProductCommand)));
    }

    @GetMapping("my")
    public ResponseEntity<List<ProductResponseDto>> getProducts(
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                productService.findProductsBySeller(new UserId(user.userId())).stream()
                        .map(productMapper::toProductResponseDto)
                        .toList()
        );
    }

}
