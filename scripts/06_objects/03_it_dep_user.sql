declare
  user_id number(20) := tp_id_seq.nextval;
  it_dep_role_id number(20);
begin
  insert into tp_objects
    (object_id, object_type_id, parent_id, name)
    values
    (user_id, 9223372036854775790, null, 'itdep');

  insert into tp_params
    (object_id, attr_id, value)
    values
    (user_id, 9223372036854775788, 'vML8BzuXimUgAW5yjmMSHQ==');

  insert into tp_params
    (object_id, attr_id, value)
    values
    (user_id, 9223372036854775787, 'itdep_user_salt');

  select
    object_id into it_dep_role_id
  from
    tp_objects
  where
    name = 'IT Department';

  insert into tp_params
      (object_id, attr_id, reference)
      values
      (user_id, 9223372036854775786, it_dep_role_id);

  commit;
end;