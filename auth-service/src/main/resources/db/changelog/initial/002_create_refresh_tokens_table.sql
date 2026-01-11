--chagaiNikita
create table refresh_tokens(
    id uuid primary key,
    user_id uuid not null,
    token varchar not null,
    expires_at timestamp not null
)