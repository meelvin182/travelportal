/
--city object type
DECLARE
  city_obj_id NUMBER(20) := TP_ID_SEQ.nextval;
BEGIN
  INSERT INTO TP_OBJECT_TYPES VALUES (city_obj_id, NULL, 'city', null);
  
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'country', 1, NULL, NULL);
  INSERT INTO TP_ATTR_OBJ_TYPES VALUES (city_obj_id, TP_ID_SEQ.currval);
  COMMIT;
END;
/