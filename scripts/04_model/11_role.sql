/
--role object type
BEGIN
  INSERT INTO TP_OBJECT_TYPES VALUES (TP_ID_SEQ.nextval, NULL, 'role', NULL);
  COMMIT;
END;
/