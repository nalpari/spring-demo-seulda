CREATE TABLE IF NOT EXISTS `users` (
    age integer,
    created_at timestamp(6),
    id bigint generated by default as identity,
    updated_at timestamp(6),
    email varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    primary key (id)
);