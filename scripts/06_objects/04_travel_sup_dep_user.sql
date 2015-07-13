declare
  user_id number(20) := tp_id_seq.nextval;
  travel_dep_role_id number(20);
begin
  insert into tp_objects
    (object_id, object_type_id, parent_id, name)
    values
    (user_id, 9223372036854775790, null, 'traveldep');

  insert into tp_params
    (object_id, attr_id, value)
    values
    (user_id, 9223372036854775788, '6WDq2KtG0H4hTcZe3tuJ/Q==');

  insert into tp_params
    (object_id, attr_id, value)
    values
    (user_id, 9223372036854775787, 'traveldep_user_salt');

  select
    object_id into travel_dep_role_id
  from
    tp_objects
  where
    name = 'Travel Support Department';

  insert into tp_params
      (object_id, attr_id, reference)
      values
      (user_id, 9223372036854775786, travel_dep_role_id);

  commit;
end;