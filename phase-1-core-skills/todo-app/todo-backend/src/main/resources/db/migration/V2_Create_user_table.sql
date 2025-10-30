CREATE TABLE app_user (
                          id          BIGSERIAL PRIMARY KEY,
                          username    VARCHAR(255) NOT NULL UNIQUE,
                          password    VARCHAR(255) NOT NULL,
                          role        VARCHAR(50) NOT NULL,
                          created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_app_user_username ON app_user (username);