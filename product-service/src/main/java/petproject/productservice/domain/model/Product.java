package petproject.productservice.domain.model;

import java.util.UUID;

public class Product {
    private final ProductId id;
    private final UserId sellerId;
    private String name;
    private String description;
    private Category category;
    private Money price;
    private int stock;
    private int reserved;
    private ProductStatus status;
    private Long version;


    private Product(ProductId id, UserId sellerId, String name, String description, Category category, Money price, int stock, int reserved, ProductStatus status, Long version) {
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.reserved = reserved;
        this.status = status;
        this.version = version;
    }

    public static Product create(UserId sellerId, String name, String description, Category category, Money price, int stock) {
        ProductId id = new ProductId(UUID.randomUUID());

        return new Product(id, sellerId, name, description, category, price, stock, 0, ProductStatus.ACTIVE, 0L);
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


    public void update(String name, String description, Category category, Money price) {
        this.name = name;
        this.description = description;
        this.category = category;
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

    public Category getCategory() {
        return category;
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

    public Long getVersion() {
        return version;
    }
}
