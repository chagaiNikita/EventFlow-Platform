--chagaiNikita
create table refresh_tokens(
    id uuid primary key,
    user_id uuid not null,
    constraint fk_refresh_tokens_credentials
    foreign key (user_id)
    references credentials(id)
)