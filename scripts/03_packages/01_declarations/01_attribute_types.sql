-- package model_at
create or replace package model_at
as
	/* see ru.ipccenter.travelportal.metamodel.entities.MMAttributeType.DataType enumeration */
	text_t constant NUMBER(10)      := 1;
	decimal_t constant NUMBER(10)   := 2;
	double_t constant NUMBER(10)    := 3;
	datetime_t constant NUMBER(10)  := 4;
	masked_t constant NUMBER(10)    := 5;
	list_t constant NUMBER(10)      := 6;
	reference_t constant NUMBER(10) := 7;
  bool_t constant NUMBER(10)      := 8;

	procedure fill_attr_type_table_base;

	function text return number;
	function decimal return number;
	function double return number;
	function datetime return number;
	function masked return number;
	function list return number;
	function reference return number;
  function bool return number;

	function type_id(dt IN NUMBER) return NUMBER;
	function type_id(type_name IN VARCHAR2, dt IN NUMBER) return NUMBER;
	function type_id_by_list_type(list_type_id IN NUMBER) return NUMBER;
end model_at;
/