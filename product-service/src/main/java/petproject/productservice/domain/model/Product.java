package petproject.productservice.domain.model;

import petproject.productservice.domain.exception.ActionNotValidException;

import java.util.UUID;

public class Product {
    private final ProductId id;
    private final UserId sellerId;
    private String name;
    private String description;
    private CategoryId categoryId;
    private Money price;
    private int stock;
    private int reserved;
    private ProductStatus status;


    private Product(ProductId id, UserId sellerId, String name, String description, CategoryId categoryId, Money price, int stock, int reserved, ProductStatus status) {
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.stock = stock;
        this.reserved = reserved;
        this.status = status;
    }

    public static Product create(UserId sellerId, String name, String description, CategoryId categoryId, Money price, int stock) {
        ProductId id = new ProductId(UUID.randomUUID());

        return new Product(id, sellerId, name, description, categoryId, price, stock, 0, ProductStatus.ACTIVE);
    }

    public static Product restore(
            ProductId id,
            UserId sellerId,
            String name,
            String description,
            CategoryId categoryId,
            Money price,
            int stock,
            int reserved,
            ProductStatus status
    ) {
        return new Product(id, sellerId, name, description, categoryId, price, stock, reserved, status);
    }

    public void upTheStock(int amount) {
        if (amount > 0) {
            this.stock += amount;
        }
    }

    public void removeFromSale() {
        if (reserved != 0) {
            throw new IllegalArgumentException("Нельзя снять с продажи пока есть зарезервированные товары");
        }
        this.status = ProductStatus.INACTIVE;
    }


    public void authorUpdate(UserId sellerId, String name, String description, CategoryId categoryId, Money price) {
        if (!sellerId.equals(this.sellerId)) {
            throw new ActionNotValidException();
        }
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
    }



    public ProductId getId() {
        return id;
    }

    public UserId getSellerId() {
        return sellerId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }

    public Money getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getReserved() {
        return reserved;
    }

    public ProductStatus getStatus() {
        return status;
    }

}
