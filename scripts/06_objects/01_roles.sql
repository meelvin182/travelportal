DECLARE
  role_obj_type_id NUMBER(20);
  tabs_attr_id NUMBER(20);
  role_id NUMBER(20);
  tab_employee_id NUMBER(20);
  tab_reports_id NUMBER(20);

  tab_travel_manager_id NUMBER(20);
  tab_administrator_id NUMBER(20);
BEGIN
  select
    object_type_id into role_obj_type_id
  from
    tp_object_types
  where
    name = 'role';

  select
    attr_id into tabs_attr_id
  from
    tp_attributes
  where
    name = 'tabs'
    and attr_type_id = model_at.type_id('Role Tabs', model_at.list);

  select
    list_value_id into tab_employee_id
  from
    tp_list_values
  where
    list_value_type_id = (select list_value_type_id from TP_ATTRIBUTE_TYPES where attr_type_id = model_at.type_id('Role Tabs', model_at.list))
    and value = 'Employee';

  select
    list_value_id into tab_reports_id
  from
    tp_list_values
  where
    list_value_type_id = (select list_value_type_id from TP_ATTRIBUTE_TYPES where attr_type_id = model_at.type_id('Role Tabs', model_at.list))
    and value = 'Reports';

  select
    list_value_id into tab_travel_manager_id
  from
    tp_list_values
  where
    list_value_type_id = (select list_value_type_id from TP_ATTRIBUTE_TYPES where attr_type_id = model_at.type_id('Role Tabs', model_at.list))
    and value = 'Travel Manager';

  select
    list_value_id into tab_administrator_id
  from
    tp_list_values
  where
    list_value_type_id = (select list_value_type_id from TP_ATTRIBUTE_TYPES where attr_type_id = model_at.type_id('Role Tabs', model_at.list))
    and value = 'Administrator';


  BEGIN
    -- Employee (Employee, Reports)
    role_id := TP_ID_SEQ.nextval;
    INSERT INTO tp_objects
      (object_id, object_type_id, parent_id, name)
    VALUES
      (role_id, role_obj_type_id, null, 'Employee');

    INSERT INTO TP_PARAMS
      (object_id, attr_id, list_value_id, order_num)
    VALUES
      (role_id, tabs_attr_id, tab_employee_id, 1);

    INSERT INTO TP_PARAMS
      (object_id, attr_id, list_value_id, order_num)
    VALUES
      (role_id, tabs_attr_id, tab_reports_id, 2);

    COMMIT;
    EXCEPTION WHEN OTHERS
      THEN NULL;
  END;


  BEGIN
    -- Common Department (Employee, Reports)
    role_id := TP_ID_SEQ.nextval;
    INSERT INTO tp_objects
      (object_id, object_type_id, parent_id, name)
    VALUES
      (role_id, role_obj_type_id, null, 'Common Department');

    INSERT INTO TP_PARAMS
      (object_id, attr_id, list_value_id, order_num)
    VALUES
      (role_id, tabs_attr_id, tab_employee_id, 1);

    INSERT INTO TP_PARAMS
      (object_id, attr_id, list_value_id, order_num)
    VALUES
      (role_id, tabs_attr_id, tab_reports_id, 2);


    COMMIT;
    EXCEPTION WHEN OTHERS
      THEN NULL;
  END;

  BEGIN
    -- Travel Support Department(Employee, Reports, Travel Manager)
    role_id := TP_ID_SEQ.nextval;
    INSERT INTO tp_objects
    (object_id, object_type_id, parent_id, name)
    VALUES
      (role_id, role_obj_type_id, null, 'Travel Support Department');

    INSERT INTO TP_PARAMS
      (object_id, attr_id, list_value_id, order_num)
    VALUES
      (role_id, tabs_attr_id, tab_employee_id, 1);

    INSERT INTO TP_PARAMS
      (object_id, attr_id, list_value_id, order_num)
    VALUES
      (role_id, tabs_attr_id, tab_reports_id, 2);


    INSERT INTO TP_PARAMS
      (object_id, attr_id, list_value_id, order_num)
    VALUES
      (role_id, tabs_attr_id, tab_travel_manager_id, 3);

    COMMIT;
    EXCEPTION WHEN OTHERS
      THEN NULL;
  END;

  BEGIN
    -- IT Department(Employee, Reports, Administrator)
    role_id := TP_ID_SEQ.nextval;
    INSERT INTO tp_objects
    (object_id, object_type_id, parent_id, name)
    VALUES
      (role_id, role_obj_type_id, null, 'IT Department');

    INSERT INTO TP_PARAMS
      (object_id, attr_id, list_value_id, order_num)
    VALUES
      (role_id, tabs_attr_id, tab_employee_id, 1);

    INSERT INTO TP_PARAMS
      (object_id, attr_id, list_value_id, order_num)
    VALUES
      (role_id, tabs_attr_id, tab_reports_id, 2);


    INSERT INTO TP_PARAMS
      (object_id, attr_id, list_value_id, order_num)
    VALUES
      (role_id, tabs_attr_id, tab_administrator_id, 3);

    COMMIT;
    EXCEPTION WHEN OTHERS
      THEN NULL;
  END;

END;
/
