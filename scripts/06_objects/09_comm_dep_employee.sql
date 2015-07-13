DECLARE
  employee_id number(20) := tp_id_seq.nextval;
 BEGIN
  INSERT INTO tp_objects
            (object_id, object_type_id, parent_id, name)
            VALUES
            (employee_id, 9223372036854775798, null, employee1);
  insert into tp_params
            (object_id, attr_id, value)
            VALUES
            (employee_id, 9223372036854775797, 'Ann');
  insert into tp_params
            (object_id, attr_id, value)
            VALUES
            (employee_id, 9223372036854775796, 'Smith');
  insert into tp_params
            (object_id, attr_id, reference)
            VALUES
            (employee_id, 9223372036854775795, 9223372036854775365);
  insert into tp_params
            (object_id, attr_id, reference)
            VALUES
            (employee_id, 9223372036854775791, 9223372036854775722);
  commit;
END;
