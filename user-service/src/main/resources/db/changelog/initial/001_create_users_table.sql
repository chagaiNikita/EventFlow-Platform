--chagaiNikita

create table users(
    id uuid primary key,
    email varchar(255) not null,
    first_name varchar(55) not null,
    last_name varchar(55) not null,
    created_at timestamp not null,
    updated_at timestamp not null
);