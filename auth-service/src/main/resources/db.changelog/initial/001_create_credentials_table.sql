--chagaiNikita
create table credentials(
    id uuid primary key,
    user_id uuid not null,
    email varchar(255) unique not null,
    password_hash varchar not null,
    created_at timestamp not null
)
