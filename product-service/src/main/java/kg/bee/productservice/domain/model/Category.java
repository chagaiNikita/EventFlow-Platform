package kg.bee.productservice.domain.model;

import java.util.UUID;

public class Category {
    private final CategoryId id;
    private String name;


    public Category(CategoryId id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public CategoryId getId() {
        return id;
    }

    public static Category create(String name) {
        return new Category(new CategoryId(UUID.randomUUID()), name);
    }
}
