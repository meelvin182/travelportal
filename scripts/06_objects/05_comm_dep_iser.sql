declare
  user_id number(20) := tp_id_seq.nextval;
  comm_dep_role_id number(20);
begin
  insert into tp_objects
    (object_id, object_type_id, parent_id, name)
    values
    (user_id, 9223372036854775790, null, 'commdep');

  insert into tp_params
    (object_id, attr_id, value)
    values
    (user_id, 9223372036854775788, 'br7iStwU/oYhS2woPBJ3gw==');

  insert into tp_params
    (object_id, attr_id, value)
    values
    (user_id, 9223372036854775787, 'commdep_user_salt');

  select
    object_id into comm_dep_role_id
  from
    tp_objects
  where
    name = 'Common Department';

  insert into tp_params
      (object_id, attr_id, reference)
      values
      (user_id, 9223372036854775786, comm_dep_role_id);

  commit;
end;