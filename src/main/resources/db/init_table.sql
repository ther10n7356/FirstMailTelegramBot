create table tgmail_users(
  tg_user_id integer,
  tg_user char(100),
  tg_chat_id integer,
  tg_user_name char(500)
);

create sequence tg_user_id_seq start 1001;

create table tgmail_properties(
  tg_prop_id integer,
  tg_user_id integer,
  tg_mail_host char(100),
  tg_email char(100),
  tg_email_password char(500)
); 

create sequence tg_prop_id_seq start 1001;

create table tgmail_active_chat(
  ac_id integer,
  ac_user_id integer
);

create sequence tg_active_chat_id_seq start 1001;

--drop table tgmail_properties;
--drop TABLE tgmail_users;
--drop table tgmail_active_chat;
--drop procedure add_new_tg_user;
drop procedure add_active_chat;

create or replace
procedure add_new_tg_user (
  i_tg_user char,
  i_tg_chat_id bigint,
  i_tg_user_name char
)
as $$
declare
  v_user_id integer;

v_exists integer;

begin 
   select
	count(*)
     into
	v_exists
from
	tgmail_users
where
	tg_user = i_tg_user
 and tg_chat_id = i_tg_chat_id;

if v_exists = 0
then
	select
	nextval('tg_user_id_seq')
into
	v_user_id;

insert
	into
	tgmail_users (
tg_user_id,
	tg_user,
	tg_chat_id,
	tg_user_name
)
values (
v_user_id,
i_tg_user,
i_tg_chat_id,
i_tg_user_name
);
end if;
end;

$$ language plpgsql;

create or replace procedure 
add_tg_mail_properties (
i_tg_user char,
i_tg_mail_host char,
i_tg_email char,
i_tg_email_password char
)
as $$
declare
  v_user_id integer;
  v_prop_id integer;
begin
	select
	tg_user_id
    into
	v_user_id
from
	tgmail_users
where
	tg_user = i_tg_user;

select
	nextval('tg_prop_id_seq')
into
	v_prop_id;

insert
	into
	tgmail_properties(
  tg_prop_id,
	tg_user_id,
	tg_mail_host,
	tg_email,
	tg_email_password
)
values (
  v_prop_id,
v_user_id,
i_tg_mail_host,
i_tg_email,
i_tg_email_password
);

exception
when no_data_found then
  raise exception 'Пользователь % не найден',
i_tg_user;
end;

$$ language plpgsql;

create or replace
procedure
add_active_chat(
i_tg_chat_id bigint,
i_tg_login varchar
)
as $$
declare
  v_ac_id integer;
  v_user_id integer;
begin
begin
  select
	tg_user_id
    into
	v_user_id
from
	tgmail_users
where
	tg_chat_id = i_tg_chat_id
  and tg_user = i_tg_login;
exception
when no_data_found then
  v_user_id := -1;
end;

if v_user_id != -1 then
  select
	nextval('tg_active_chat_id_seq')
into
	v_ac_id; 

insert
	into
	tgmail_active_chat(
ac_id,
	ac_user_id
)
values (
v_ac_id,
v_user_id
);
end if;
end;

$$ language plpgsql;
