package petproject.productservice.interfaces.api;

import jakarta.validation.Valid;
import petproject.productservice.application.service.CategoryService;
import petproject.productservice.domain.model.CategoryId;
import petproject.productservice.interfaces.api.dto.CategoryResponseDto;
import petproject.productservice.interfaces.api.dto.CategoryRequestDto;
import petproject.productservice.interfaces.api.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;


    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(
                categoryService.getAllCategories().stream().map(categoryMapper::categoryToCategoryDto).toList()
        );
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                categoryMapper.categoryToCategoryDto(categoryService.createCategory(categoryRequestDto.name()))
        );
    }

    @PutMapping("{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto, @PathVariable UUID categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                categoryMapper.categoryToCategoryDto(categoryService.changeCategoryName(new CategoryId(categoryId), categoryRequestDto.name()))
        );
    }



}
