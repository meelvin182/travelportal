DECLARE
  city_id number(20) := tp_id_seq.nextval;
  country_id number(20);

BEGIN
  insert into tp_objects
    (object_id, object_type_id, parent_id, name)
    VALUES
    (city_id, 9223372036854775780, null, 'Moscow');

  select
    object_id into country_id
    from tp_objects
    Where name = 'Russia';

  insert into tp_params
    (object_id, attr_id, reference)
    VALUES
    (city_id, 9223372036854775779, country_id);

    commit;
    END;