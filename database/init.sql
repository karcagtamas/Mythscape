CREATE DATABASE mythscape;

CREATE USER mythscape WITH PASSWORD 'mythscape';
GRANT ALL PRIVILEGES ON DATABASE mythscape TO mythscape;

CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(80)  NOT NULL,
    email    VARCHAR(120) NOT NULL,
    username VARCHAR(24)  NOT NULL,
    password VARCHAR(255) NOT NULL,
    register DATE         NOT NULL DEFAULT CURRENT_DATE,
    UNIQUE (username)
);

CREATE TABLE campaigns
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(40) NOT NULL,
    description TEXT,
    creator_id  INTEGER     NOT NULL REFERENCES users (id),
    creation    DATE        NOT NULL DEFAULT CURRENT_DATE,
    last_update DATE        NOT NULL DEFAULT CURRENT_DATE,
    UNIQUE (name, creator_id)
);

CREATE TABLE campaign_members
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(80) NOT NULL,
    campaign_id INTEGER     NOT NULL REFERENCES campaigns (id),
    user_id     INTEGER REFERENCES users (id),
    creation    DATE        NOT NULL DEFAULT CURRENT_DATE,
    is_dm       BOOLEAN              DEFAULT FALSE
);

CREATE TABLE campaign_tags
(
    id          SERIAL PRIMARY KEY,
    campaign_id INTEGER     NOT NULL REFERENCES campaigns (id),
    caption     VARCHAR(40) NOT NULL,
    color       VARCHAR(7)  NOT NULL,
    creation    DATE        NOT NULL DEFAULT CURRENT_DATE,
    UNIQUE (campaign_id, caption)
);

CREATE TABLE sessions
(
    id          SERIAL PRIMARY KEY,
    date        DATE    NOT NULL DEFAULT CURRENT_DATE,
    start_time  TIME    NOT NULL,
    end_time    TIME    NOT NULL,
    campaign_id INTEGER NOT NULL REFERENCES campaigns (id)
);

CREATE TABLE note_categories
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(40) NOT NULL,
    creation        DATE        NOT NULL DEFAULT CURRENT_DATE,
    creator_id      INTEGER     NOT NULL REFERENCES users (id),
    last_update     DATE        NOT NULL DEFAULT CURRENT_DATE,
    last_updater_id INTEGER     NOT NULL REFERENCES users (id),
    color           VARCHAR(7)  NOT NULL,
    UNIQUE (name)
);

CREATE TABLE notes
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(120) NOT NULL,
    creator_id  INTEGER      NOT NULL REFERENCES users (id),
    campaign_id INTEGER      NOT NULL REFERENCES campaigns (id),
    category_id INTEGER REFERENCES note_categories (id),
    session_id  INTEGER REFERENCES sessions (id),
    content     TEXT         NOT NULL,
    creation    DATE         NOT NULL DEFAULT CURRENT_DATE,
    last_update DATE         NOT NULL DEFAULT CURRENT_DATE
);