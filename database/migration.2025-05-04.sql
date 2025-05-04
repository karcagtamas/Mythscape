alter table campaigns
drop constraint campaigns_creator_id_fkey;

alter table campaigns
    add foreign key (creator_id) references users
        on delete cascade;

alter table campaigns
drop constraint campaigns_image_id_fkey;

alter table campaigns
    add foreign key (image_id) references files
        on delete set null;

alter table campaign_members
drop constraint campaign_members_campaign_id_fkey;

alter table campaign_members
    add foreign key (campaign_id) references campaigns
        on delete cascade;

alter table campaign_members
drop constraint campaign_members_user_id_fkey;

alter table campaign_members
    add foreign key (user_id) references users
        on delete set null;

alter table campaign_tags
drop constraint campaign_tags_campaign_id_fkey;

alter table campaign_tags
    add foreign key (campaign_id) references campaigns
        on delete cascade;

alter table notes
drop constraint notes_campaign_id_fkey;

alter table notes
    add foreign key (campaign_id) references campaigns
        on delete cascade;

alter table notes
drop constraint notes_category_id_fkey;

alter table notes
    add foreign key (category_id) references note_categories
        on delete set null;

alter table notes
drop constraint notes_session_id_fkey;

alter table notes
    add foreign key (session_id) references sessions
        on delete set null;

alter table sessions
drop constraint sessions_campaign_id_fkey;

alter table sessions
    add foreign key (campaign_id) references campaigns
        on delete cascade;

create table folders
(
    id          serial
        constraint folders_pk
            primary key,
    name        varchar(256)            not null,
    parent_id   integer
        constraint folders_folders_id_fk
            references folders
            on delete cascade,
    campaign_id integer                 not null
        constraint folders_campaigns_id_fk
            references campaigns
            on delete cascade,
    category_id integer
        constraint folders_note_categories_id_fk
            references note_categories
            on delete set null,
    session_id  integer
        constraint folders_sessions_id_fk
            references sessions
            on delete set null,
    creation    timestamp default now() not null,
    last_update timestamp default now() not null
);


GRANT ALL PRIVILEGES ON TABLE folders TO mythscape;
GRANT ALL PRIVILEGES ON SEQUENCE folders_id_seq TO mythscape;

alter table notes
    add folder_id integer
        constraint notes_folders_id_fk
            references folders
            on delete cascade;

alter table notes
    drop constraint notes_category_id_fkey;

alter table notes
    drop column category_id;

alter table notes
    drop constraint notes_session_id_fkey;

alter table notes
    drop column session_id;

