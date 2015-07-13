-- cleanup
BEGIN
--    DELETE FROM TP_ATTR_OBJ_TYPES;
--    DELETE FROM TP_PARAMS;
--
--    DELETE FROM TP_OBJECTS;
--    DELETE FROM TP_OBJECT_TYPES;
--
--    DELETE FROM TP_ATTRIBUTES;
--    DELETE FROM TP_LIST_VALUES;
--    DELETE FROM TP_ATTRIBUTE_TYPES;

   FOR cur_rec IN (SELECT 
                     object_name, object_type
                   FROM 
                     user_objects
                   WHERE 
                     object_type IN ('VIEW', 'PACKAGE', 'PROCEDURE', 'FUNCTION', 'SEQUENCE', 'TABLE')
                     and object_name like 'TP%')
   LOOP
      BEGIN
         IF cur_rec.object_type = 'TABLE'
         THEN
            DBMS_OUTPUT.put_line ('DROP ' || cur_rec.object_type || ' "' || cur_rec.object_name || '" CASCADE CONSTRAINTS');
            EXECUTE IMMEDIATE 'DROP ' || cur_rec.object_type || ' "' || cur_rec.object_name || '" CASCADE CONSTRAINTS';
--             NULL;
         ELSE
            DBMS_OUTPUT.put_line ('DROP ' || cur_rec.object_type || ' "' || cur_rec.object_name || '" CASCADE CONSTRAINTS');
            EXECUTE IMMEDIATE 'DROP ' || cur_rec.object_type || ' "' || cur_rec.object_name || '"';
         END IF;
      EXCEPTION
         WHEN OTHERS
         THEN
            DBMS_OUTPUT.put_line (   'FAILED: DROP ' || cur_rec.object_type || ' "' || cur_rec.object_name || '"' );
      END;
   END LOOP;
END;
/