CREATE TABLE todo (
                      id          BIGSERIAL PRIMARY KEY,
                      title       VARCHAR(255) NOT NULL,
                      completed   BOOLEAN NOT NULL DEFAULT FALSE,
                      created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_todo_completed ON todo (completed);