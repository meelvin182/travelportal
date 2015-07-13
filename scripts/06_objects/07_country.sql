DECLARE
  country_id number(20) := tp_id_seq.nextval;

BEGIN
  insert into tp_objects
    (object_id, object_type_id, parent_id, name)
    VALUES
    (country_id, 9223372036854775755, null, 'Russia');
    commit;
END;