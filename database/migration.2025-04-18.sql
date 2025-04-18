alter table refresh_tokens
    alter column expiration type timestamp using expiration::timestamp;

alter table refresh_tokens
    alter column revoked type timestamp using revoked::timestamp;

alter table users
    alter column register type timestamp using register::timestamp;

alter table users
    alter column register set default now();

alter table notes
    alter column creation type timestamp using creation::timestamp;

alter table notes
    alter column creation set default now();

alter table notes
    alter column last_update type timestamp using last_update::timestamp;

alter table notes
    alter column last_update set default now();

alter table note_categories
    alter column creation type timestamp using creation::timestamp;

alter table note_categories
    alter column creation set default now();

alter table note_categories
    alter column last_update type timestamp using last_update::timestamp;

alter table note_categories
    alter column last_update set default now();

alter table campaigns
    alter column creation type timestamp using creation::timestamp;

alter table campaigns
    alter column creation set default now();

alter table campaigns
    alter column last_update type timestamp using last_update::timestamp;

alter table campaigns
    alter column last_update set default now();

alter table campaign_tags
    alter column creation type timestamp using creation::timestamp;

alter table campaign_tags
    alter column creation set default now();

alter table campaign_members
    alter column creation type timestamp using creation::timestamp;

alter table campaign_members
    alter column creation set default now();
