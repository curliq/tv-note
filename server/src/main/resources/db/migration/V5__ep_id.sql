alter table if exists tracked_episodes drop constraint FK1tl34598si8jbuvgynimkqdks;
alter table if exists stored_episodes alter column id set data type integer USING (id::integer);
alter table if exists tracked_episodes alter column id set data type varchar(255);
alter table if exists tracked_episodes alter column storedepisode_id set data type integer USING (storedepisode_id::integer);
alter table if exists tracked_episodes add constraint FK1tl34598si8jbuvgynimkqdkg foreign key (storedepisode_id) references stored_episodes;
