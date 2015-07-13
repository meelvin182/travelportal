/
--trf object type
DECLARE
  trf_obj_id NUMBER(20) := TP_ID_SEQ.nextval;
  status_lv_type_id NUMBER(20);
  employee_attr_id NUMBER(20);
BEGIN
  INSERT INTO TP_OBJECT_TYPES VALUES (trf_obj_id, NULL, 'trf', NULL);
  
  SELECT ATTR_ID
  INTO employee_attr_id
  FROM
    TP_ATTRIBUTES
  WHERE NAME = 'employee';
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (trf_obj_id, employee_attr_id);

  select
    LIST_VALUE_TYPE_ID into status_lv_type_id
  from
    TP_ATTRIBUTE_TYPES
  where
    NAME = 'StatusType';

  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id_by_list_type(status_lv_type_id), 'status', 2, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (trf_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'project_manager', 3, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (trf_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.datetime), 'departure_date', 4, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (trf_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.datetime), 'return_date', 5, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (trf_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'destination_city', 6, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (trf_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.bool), 'car_rent', 7, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (trf_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.bool), 'pay_cash', 8, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (trf_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.text), 'hotel', 9, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (trf_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'customer', 10, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (trf_obj_id, TP_ID_SEQ.currval);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'status_history', 11, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (trf_obj_id, TP_ID_SEQ.currval);
  COMMIT;
END;
/
