 /
-- employee object type
DECLARE
  employee_obj_id NUMBER(20) := TP_ID_SEQ.nextval;
  department_attr_id NUMBER(20);
BEGIN
  INSERT INTO TP_OBJECT_TYPES VALUES (employee_obj_id, NULL, 'employee', NULL);

  SELECT ATTR_ID
  INTO department_attr_id
  FROM
    TP_ATTRIBUTES
  WHERE NAME = 'department';
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (employee_obj_id, department_attr_id);
  
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.text), 'first_name', 2, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (employee_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.text), 'last_name', 3, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (employee_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'office', 4, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (employee_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'manager', 5, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (employee_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'position', 6, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (employee_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.text), 'email', 7, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (employee_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'user', 8, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (employee_obj_id, TP_ID_SEQ.currval);
  COMMIT;
END;
/
