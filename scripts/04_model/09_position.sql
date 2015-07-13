/
--position object type
DECLARE
  position_obj_id    NUMBER(20) := TP_ID_SEQ.nextval;
  department_attr_id NUMBER(20);
BEGIN
  INSERT INTO TP_OBJECT_TYPES VALUES (position_obj_id, NULL, 'position', NULL);

  SELECT ATTR_ID
  INTO department_attr_id
  FROM
    TP_ATTRIBUTES
  WHERE NAME = 'department';
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (position_obj_id, department_attr_id);
  COMMIT;
END;
/