package kg.bee.productservice.interfaces.api;

import jakarta.validation.Valid;
import kg.bee.productservice.interfaces.api.dto.CreateProductRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/products")
public class ProductController {

    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRequestDto createProductRequestDto) {
        return ResponseEntity.ok().build();
    }
}
