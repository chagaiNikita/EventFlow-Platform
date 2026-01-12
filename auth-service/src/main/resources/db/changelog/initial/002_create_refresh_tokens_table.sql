--chagaiNikita
create table refresh_tokens(
    id uuid primary key,
    user_id uuid not null,
    token varchar(255) not null unique,
    expires_at timestamp not null,
    created_at timestamp not null
);

CREATE INDEX idx_refresh_token_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_token_expires_at ON refresh_tokens(expires_at);