/
--status history update
DECLARE
  datetime_type_id NUMBER(20);
  status_history_obj_type_id NUMBER(20);
  user_attribute_id NUMBER(20);
BEGIN
  SELECT object_type_id
  INTO status_history_obj_type_id
  FROM TP_OBJECT_TYPES
  WHERE NAME = 'status_history';

  SELECT attr_id
  INTO user_attribute_id
  FROM TP_ATTRIBUTES
  WHERE NAME = 'user';

  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.datetime), 'change_time', 2, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (status_history_obj_type_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.text), 'comment', 3, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (status_history_obj_type_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (status_history_obj_type_id, user_attribute_id);

  COMMIT;
END;
/