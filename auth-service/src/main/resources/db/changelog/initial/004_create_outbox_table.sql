CREATE TABLE outbox
(
    id         UUID PRIMARY KEY,
    event_type VARCHAR(255) NOT NULL,
    payload    JSONB        NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL,
    processed  BOOLEAN      NOT NULL DEFAULT FALSE
);