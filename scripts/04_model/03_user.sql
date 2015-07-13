/
-- user object type
DECLARE
  user_obj_id NUMBER(20) := TP_ID_SEQ.nextval;
BEGIN
  INSERT INTO TP_OBJECT_TYPES VALUES (user_obj_id, NULL, 'user', NULL);
  
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.text), 'username', 2, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (user_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.text), 'password', 3, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (user_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.text), 'salt', 4, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (user_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'role', 2, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (user_obj_id, TP_ID_SEQ.currval);
  COMMIT;
END;
/