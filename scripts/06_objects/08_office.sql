DECLARE
  office_id number(20) := tp_id_seq.nextval;
  city_id number(20);

BEGIN
  insert into tp_objects
    (object_id, object_type_id, parent_id, name)
    VALUES
    (office_id, 9223372036854775783, null, 'office1');

  select
    object_id into city_id
    from tp_objects
    Where name = 'Moscow';

  insert into tp_params
    (object_id, attr_id, reference)
    VALUES
    (office_id, 9223372036854775779, city_id);

  insert into tp_params
    (object_id, attr_id, value)
    VALUES
    (office_id, 9223372036854775781, 'Pervomayskaya');

    commit;
    END;