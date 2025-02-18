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

GRANT ALL PRIVILEGES ON TABLE users TO mythscape;
GRANT ALL PRIVILEGES ON SEQUENCE users_id_seq TO mythscape;

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

GRANT ALL PRIVILEGES ON TABLE campaigns TO mythscape;
GRANT ALL PRIVILEGES ON SEQUENCE campaigns_id_seq TO mythscape;

CREATE TABLE campaign_members
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(80) NOT NULL,
    campaign_id INTEGER     NOT NULL REFERENCES campaigns (id),
    user_id     INTEGER REFERENCES users (id),
    creation    DATE        NOT NULL DEFAULT CURRENT_DATE,
    is_dm       BOOLEAN              DEFAULT FALSE
);

GRANT ALL PRIVILEGES ON TABLE campaign_members TO mythscape;
GRANT ALL PRIVILEGES ON SEQUENCE campaign_members_id_seq TO mythscape;

CREATE TABLE campaign_tags
(
    id          SERIAL PRIMARY KEY,
    campaign_id INTEGER     NOT NULL REFERENCES campaigns (id),
    caption     VARCHAR(40) NOT NULL,
    color       VARCHAR(7)  NOT NULL,
    creation    DATE        NOT NULL DEFAULT CURRENT_DATE,
    UNIQUE (campaign_id, caption)
);

GRANT ALL PRIVILEGES ON TABLE campaign_tags TO mythscape;
GRANT ALL PRIVILEGES ON SEQUENCE campaign_tags_id_seq TO mythscape;

CREATE TABLE sessions
(
    id          SERIAL PRIMARY KEY,
    date        DATE    NOT NULL DEFAULT CURRENT_DATE,
    start_time  TIME    NOT NULL,
    end_time    TIME    NOT NULL,
    campaign_id INTEGER NOT NULL REFERENCES campaigns (id)
);

GRANT ALL PRIVILEGES ON TABLE sessions TO mythscape;
GRANT ALL PRIVILEGES ON SEQUENCE sessions_id_seq TO mythscape;

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

GRANT ALL PRIVILEGES ON TABLE note_categories TO mythscape;
GRANT ALL PRIVILEGES ON SEQUENCE note_categories_id_seq TO mythscape;

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

GRANT ALL PRIVILEGES ON TABLE notes TO mythscape;
GRANT ALL PRIVILEGES ON SEQUENCE notes_id_seq TO mythscape;
