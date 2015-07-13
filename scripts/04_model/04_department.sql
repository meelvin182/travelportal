/
--department object type
DECLARE
  department_obj_id NUMBER(20) := TP_ID_SEQ.nextval;
BEGIN
  INSERT INTO TP_OBJECT_TYPES VALUES (department_obj_id, NULL, 'department', null);
  
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'manager', 1, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (department_obj_id, TP_ID_SEQ.currval);

  COMMIT;
END;
/