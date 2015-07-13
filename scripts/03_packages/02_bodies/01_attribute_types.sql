-- package body model_at
create or replace package body model_at
as
	procedure fill_attr_type_table_base is
	begin
    --delete from TP_ATTRIBUTE_TYPES;
    
	  insert into TP_ATTRIBUTE_TYPES (attr_type_id, list_value_type_id, name, data_type)
	  	values (TP_ID_SEQ.nextval, null, 'Text', text_t);
		insert into TP_ATTRIBUTE_TYPES (attr_type_id, list_value_type_id, name, data_type)
	  	values (TP_ID_SEQ.nextval, null, 'Decimal', decimal_t);
	  insert into TP_ATTRIBUTE_TYPES (attr_type_id, list_value_type_id, name, data_type)
	  	values (TP_ID_SEQ.nextval, null, 'Double', double_t);
	  insert into TP_ATTRIBUTE_TYPES (attr_type_id, list_value_type_id, name, data_type)
	  	values (TP_ID_SEQ.nextval, null, 'Date/Time', datetime_t);
	  insert into TP_ATTRIBUTE_TYPES (attr_type_id, list_value_type_id, name, data_type)
	  	values (TP_ID_SEQ.nextval, null, 'Masked', masked_t);
    insert into TP_ATTRIBUTE_TYPES (attr_type_id, list_value_type_id, name, data_type)
	  	values (TP_ID_SEQ.nextval, null, 'Reference', reference_t);
    insert into TP_ATTRIBUTE_TYPES (attr_type_id, list_value_type_id, name, data_type)
	  	values (TP_ID_SEQ.nextval, null, 'Bool', bool_t);

		commit;
	end;

	function text return number is
	begin
		return text_t;
	end;

	function decimal return number is
	begin
		return decimal_t;
	end;

	function double return number is
	begin
		return double_t;
	end;

	function datetime return number is
	begin
		return datetime_t;
	end;

	function masked return number is
	begin
		return masked_t;
	end;

	function list return number is
	begin
		return list_t;
	end;

	function reference return number is
	begin
		return reference_t;
	end;

  function bool return number is
  begin
          return bool_t;
  end;

  function type_id(dt IN number) return number is
		id number(20);
	begin
		select 
			attr_type_id into id
		from
			TP_ATTRIBUTE_TYPES 
		where
			data_type = dt;

		return id;
	end;

	function type_id(type_name IN VARCHAR2, dt IN NUMBER) return number is
		id number(20);
	begin
		select
			attr_type_id into id
		from
			TP_ATTRIBUTE_TYPES
		where
			data_type = dt
			and name = type_name;

		return id;
	end;

	function type_id_by_list_type(list_type_id IN NUMBER) return number is
		id number(20);
	begin
		select
			attr_type_id into id
		from
			TP_ATTRIBUTE_TYPES
		where
			list_value_type_id = list_type_id;

		return id;
	end;
end model_at;
/