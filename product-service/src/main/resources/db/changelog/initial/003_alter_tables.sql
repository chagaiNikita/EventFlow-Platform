ALTER TABLE products
    DROP COLUMN category,
    ADD COLUMN category_id UUID not null,
    ADD CONSTRAINT fk_products_categories
        FOREIGN KEY (category_id)
            REFERENCES categories(id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE;