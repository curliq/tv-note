alter table if exists stored_episodes alter column id set data type integer;
alter table if exists stored_episodes alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists stored_episodes alter column updated_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists tracked_episodes alter column id set data type varchar(255);
alter table if exists tracked_episodes alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists tracked_episodes alter column storedepisode_id set data type integer;
alter table if exists tracked_shows alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists users alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists stored_episodes alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists stored_episodes alter column updated_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists tracked_episodes alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists tracked_shows alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists users alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists stored_episodes alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists stored_episodes alter column updated_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists tracked_episodes alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists tracked_shows alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists users alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists stored_episodes alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists stored_episodes alter column updated_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists tracked_episodes alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists tracked_shows alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists users alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists stored_episodes alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists stored_episodes alter column updated_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists tracked_episodes alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists tracked_shows alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
alter table if exists users alter column created_at_datetime set data type TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
