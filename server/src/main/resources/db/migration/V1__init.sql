CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
create table stored_episodes
(
    id                  integer                             not null,
    created_at_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
    episode_number      integer                             not null,
    season_number       integer                             not null,
    storedshow_id       integer                             not null,
    air_date            varchar(255),
    updated_at_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
    episode_name        varchar(255),
    thumbnail           varchar(255),
    primary key (id)
);
create table stored_shows
(
    id                  serial                              not null,
    created_at_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
    poster_image        varchar(255),
    status              varchar(255),
    title               varchar(255)                        not null,
    tmdb_id             integer                             not null,
    updated_at_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
    primary key (id)
);
create table tracked_episodes
(
    id                  varchar(255) not null,
    created_at_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    storedepisode_id    integer      not null,
    trackedshow_id      integer      not null,
    primary key (id)
);
create table tracked_shows
(
    id                  serial                              not null,
    created_at_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
    watchlisted         boolean                             not null,
    storedshow_id       integer                             not null,
    user_id             integer,
    primary key (id)
);
create table users
(
    id                       serial       not null,
    created_at_datetime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    username                 varchar(255) not null,
    email                    varchar(255),
    password                 varchar(255),
    role                     smallint check (role between 0 and 1),
    fcm_token                varchar(255),
    preferences_push_allowed boolean   default true,
    primary key (id)
);
alter table if exists stored_shows
    drop constraint if exists UK_9ji5tguwdy5akdr4jg9861wvc;
alter table if exists stored_shows
    add constraint UK_9ji5tguwdy5akdr4jg9861wvc unique (tmdb_id);
alter table if exists tracked_episodes
    drop constraint if exists UK9cpm26tiyim5bb42aqlgcgwnp;
alter table if exists tracked_episodes
    add constraint UK9cpm26tiyim5bb42aqlgcgwnp unique (storedepisode_id, trackedshow_id);
alter table if exists tracked_shows
    drop constraint if exists UKrk9jk357bhb8wumxn79akkwdx;
alter table if exists tracked_shows
    add constraint UKrk9jk357bhb8wumxn79akkwdx unique (storedshow_id, user_id);
alter table if exists users
    drop constraint if exists UK_6dotkott2kjsp8vw4d0m25fb7;
alter table if exists users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);
alter table if exists users
    drop constraint if exists UK_2ywh4w6lfiljcxg7w0esfj4fd;
alter table if exists users
    add constraint UK_2ywh4w6lfiljcxg7w0esfj4fd unique (username);
alter table if exists stored_episodes
    add constraint FKkptq9jjny2e63wrtk21kdhgnv foreign key (storedshow_id) references stored_shows;
alter table if exists tracked_episodes
    add constraint FK1tl34598si8jbuvgynimkqdks foreign key (storedepisode_id) references stored_episodes;
alter table if exists tracked_episodes
    add constraint FKq6eq9ur4bdkr5rcpbya9s4arx foreign key (trackedshow_id) references tracked_shows on delete cascade;
alter table if exists tracked_shows
    add constraint FK3kdaak4msxympk9ll7gcd6dij foreign key (storedshow_id) references stored_shows;
alter table if exists tracked_shows
    add constraint FKs7tcsw8wjlt08n67s46c8hd4j foreign key (user_id) references users on delete cascade;
