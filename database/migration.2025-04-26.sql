create table files
(
    id        serial PRIMARY KEY,
    name      varchar(256) not null,
    bytes     bytea        not null,
    mime_type varchar(80)  not null
);

GRANT ALL PRIVILEGES ON TABLE files TO mythscape;
GRANT ALL PRIVILEGES ON SEQUENCE files_id_seq TO mythscape;

alter table campaigns
    add title varchar(120);

alter table campaigns
    add image_id integer references files (id);
