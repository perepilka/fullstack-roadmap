alter table todo
    add column user_id bigint;

alter table todo
    alter column user_id set not null;

alter table todo
    add constraint fk_todo_user
        foreign key (user_id)
            references app_user(id);

CREATE INDEX idx_todo_user_id ON todo (user_id);