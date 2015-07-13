/
--office object type
DECLARE
  office_obj_id NUMBER(20) := TP_ID_SEQ.nextval;
BEGIN
  INSERT INTO TP_OBJECT_TYPES VALUES (office_obj_id, NULL, 'office', null);
  
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'city', 1, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (office_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.text), 'address', 2, 'Address of an office.', NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (office_obj_id, TP_ID_SEQ.currval);
  COMMIT;
END;
/