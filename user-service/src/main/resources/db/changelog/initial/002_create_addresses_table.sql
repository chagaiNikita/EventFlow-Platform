--chagaiNikita

create table addresses(
    id uuid primary key,
    user_id uuid not null,
    constraint fk_addreses_users
    foreign key (user_id)
    references users(id)
    on delete cascade
    on update cascade,
    address varchar(255) not null,
    created_at timestamp not null
);