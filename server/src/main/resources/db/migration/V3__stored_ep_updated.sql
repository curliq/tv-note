alter table if exists stored_episodes add column updated_at_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null;
