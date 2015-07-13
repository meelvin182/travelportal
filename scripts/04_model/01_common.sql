/
--common attributes
BEGIN 
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'employee', 1, NULL, NULL);
  INSERT INTO TP_ATTRIBUTES VALUES (TP_ID_SEQ.nextval, model_at.type_id(model_at.reference), 'department', 1, NULL, NULL);
  COMMIT;
END;
/