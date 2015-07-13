/
--role update
DECLARE
  tabs_lv_type_id NUMBER(20);
  tabs_type_id NUMBER(20);
  role_object_type_id NUMBER(20);
BEGIN
  tabs_lv_type_id     := TP_ID_SEQ.nextval;
  tabs_type_id        := TP_ID_SEQ.nextval;

  select
    object_type_id into role_object_type_id
  from
    tp_object_types
  where
    name = 'role';


  insert into tp_attribute_types
    (attr_type_id, list_value_type_id, name, data_type)
  values
    (tabs_type_id, tabs_lv_type_id, 'Role Tabs', model_at.list);

  insert into tp_attributes
    (attr_id, attr_type_id, name, order_num)
  values
    (tp_id_seq.nextval, tabs_type_id, 'tabs', 1);

  insert into tp_attr_obj_types
    (object_type_id, attr_id)
  values
    (role_object_type_id, tp_id_seq.currval);


  insert into tp_list_values
    (list_value_id, list_value_type_id, value, order_num)
  values
    (tp_id_seq.nextval, tabs_lv_type_id, 'Employee', 1);

  insert into tp_list_values
    (list_value_id, list_value_type_id, value, order_num)
  values
    (tp_id_seq.nextval, tabs_lv_type_id, 'Reports', 2);

  insert into tp_list_values
    (list_value_id, list_value_type_id, value, order_num)
  values
    (tp_id_seq.nextval, tabs_lv_type_id, 'Travel Manager', 3);

  insert into tp_list_values
    (list_value_id, list_value_type_id, value, order_num)
  values
    (tp_id_seq.nextval, tabs_lv_type_id, 'Administrator', 4);

  COMMIT;
END;
/


