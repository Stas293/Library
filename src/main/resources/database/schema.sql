create table authors
(
    id          bigint generated always as identity
        primary key,
    first_name  varchar(255) not null,
    last_name   varchar(255) not null,
    middle_name varchar(255)
);

create table books
(
    id               bigint generated always as identity
        primary key,
    title            varchar(255)     not null,
    description      text             not null,
    isbn             varchar(13)      not null
        unique,
    date_publication date             not null,
    fine             double precision not null,
    count            integer          not null,
    language         varchar(255)     not null,
    location         varchar(255)
        unique
);

create table book_authors
(
    author_id bigint not null
        constraint fk_book_authors_author_id
            references authors
            on delete cascade,
    book_id   bigint not null
        constraint fk_book_authors_book_id
            references books
            on delete cascade,
    constraint book_authors_pk
        primary key (author_id, book_id)
);

create table keywords
(
    id      bigint generated always as identity
        primary key,
    keyword varchar(255) not null
        unique
);

create table book_keywords
(
    book_id    bigint not null
        constraint fk_book_keywords_book_id
            references books
            on delete cascade,
    keyword_id bigint not null
        constraint fk_book_keywords_keyword_id
            references keywords
            on delete cascade,
    constraint book_keywords_pk
        primary key (book_id, keyword_id)
);

create table revision
(
    id        bigint generated always as identity
        primary key,
    timestamp bigint,
    username  varchar(255)
);

create table roles
(
    id   bigint generated always as identity
        primary key,
    code varchar(255) not null
        unique,
    name varchar(255) not null
        unique
);

create table statuses
(
    id     bigint generated always as identity
        primary key,
    code   varchar(255) not null
        unique,
    closed boolean      not null
);

create table next_statuses
(
    next_status bigint not null
        constraint fk_next_statuses_next_status
            references statuses
            on delete cascade,
    status      bigint not null
        constraint fk_next_statuses_status
            references statuses
            on delete cascade,
    constraint next_statuses_pk
        primary key (status, next_status)
);

create table users
(
    id         bigint generated always as identity
        primary key,
    login      varchar(255) not null
        unique,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    phone      varchar(13)  not null
        unique,
    email      varchar(255) not null
        unique,
    password   varchar(255) not null,
    created_at date,
    enabled    boolean      not null,
    updated_at date
);

create table history_orders
(
    id            bigint generated always as identity
        primary key,
    book_title    varchar(255) not null,
    date_created  date         not null,
    date_returned date         not null,
    status_id     bigint       not null
        constraint fk_history_orders_status_id
            references statuses
            on delete restrict,
    user_id       bigint
        constraint fk_history_orders_user_id
            references users
            on delete cascade
);

create table user_role
(
    role_id bigint not null
        constraint fk_user_role_role_id
            references roles
            on delete restrict,
    user_id bigint not null
        constraint fk_user_role_user_id
            references users
            on delete cascade,
    constraint user_role_pk
        primary key (role_id, user_id)
);

create table places
(
    id           bigint generated always as identity
        primary key,
    code         varchar(255)          not null
        constraint places_name_key
            unique,
    default_days integer default 0     not null,
    choosable    boolean default false not null
);

create table orders
(
    id           bigint generated always as identity
        primary key,
    date_created date   not null,
    date_expire  date,
    book_id      bigint not null
        references books
            on delete restrict,
    status_id    bigint not null
        references statuses
            on delete restrict,
    user_id      bigint not null
        references users
            on delete cascade,
    place_id     bigint not null
        references places
            on delete restrict
);

create table status_name
(
    id        bigint generated always as identity
        constraint status_name_pk
            primary key,
    name      varchar(255) not null,
    lang      varchar(255) not null,
    status_id bigint       not null
        constraint fk_status_name_status_id
            references statuses
            on delete cascade,
    unique (lang, status_id)
);

create table place_names
(
    id       bigint generated always as identity
        constraint place_names_pk
            primary key,
    lang     varchar(255) not null,
    name     varchar(255) not null,
    place_id bigint       not null
        constraint fk_place_names_place
            references places
            on delete cascade,
    unique (lang, place_id)
);

