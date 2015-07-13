-- create sequence

create sequence TP_ID_SEQ
	start with 9223372036854775807
	increment by -1
	minvalue 1
	maxvalue 9223372036854775807
	cycle
	cache 20;
/