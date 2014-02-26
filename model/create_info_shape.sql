CREATE TABLE info_shape(
	id serial primary key,
	shapetable character varying(100) not null,
	shapeprecision double precision default 0.01
);