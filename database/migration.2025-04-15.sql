CREATE TABLE refresh_tokens
(
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER     NOT NULL,
    client_id  VARCHAR(40) NOT NULL,
    token      VARCHAR(40) NOT NULL,
    expiration DATE        NOT NULL,
    revoked    DATE        NOT NULL,
    UNIQUE (user_id, client_id, token)
);

GRANT ALL PRIVILEGES ON TABLE refresh_tokens TO mythscape;
GRANT ALL PRIVILEGES ON SEQUENCE refresh_tokens_id_seq TO mythscape;