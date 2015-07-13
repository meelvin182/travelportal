/
--status history
DECLARE
  status_lv_type_id NUMBER(20);
  status_type_id NUMBER(20);
  status_history_obj_type_id NUMBER(20);
BEGIN
  status_lv_type_id := TP_ID_SEQ.nextval;
  status_type_id := TP_ID_SEQ.nextval;

  insert into TP_ATTRIBUTE_TYPES VALUES (status_type_id, status_lv_type_id, 'StatusType', model_at.list);
  
  INSERT INTO TP_LIST_VALUES VALUES (TP_ID_SEQ.nextval, status_lv_type_id, 'Entering', 1);
  INSERT INTO TP_LIST_VALUES VALUES (TP_ID_SEQ.nextval, status_lv_type_id, 'Ready', 2);
  INSERT INTO TP_LIST_VALUES VALUES (TP_ID_SEQ.nextval, status_lv_type_id, 'Completed', 3);
  INSERT INTO TP_LIST_VALUES VALUES (TP_ID_SEQ.nextval, status_lv_type_id, 'Rejected', 4);
  INSERT INTO TP_LIST_VALUES VALUES (TP_ID_SEQ.nextval, status_lv_type_id, 'Canceled', 5);
  
  status_history_obj_type_id := TP_ID_SEQ.nextval;
  
  INSERT INTO TP_OBJECT_TYPES VALUES (status_history_obj_type_id, NULL, 'status_history', NULL);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, status_type_id, 'status', 1, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (status_history_obj_type_id, TP_ID_SEQ.currval);
  
  COMMIT;
END;
/