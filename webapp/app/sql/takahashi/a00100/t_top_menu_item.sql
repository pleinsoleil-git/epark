DROP TABLE t_top_menu_item CASCADE;


CREATE TABLE t_top_menu_item
(
	id						BIGSERIAL,
	foreign_id				BIGINT				REFERENCES t_top_menu( id ) ON DELETE CASCADE,
	title					VARCHAR( 1024 ),
	created_at				TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY
	(
		id
	)
	WITH
	(
		FILLFACTOR = 100
	)
)
WITH
(
	FILLFACTOR = 100
)
TABLESPACE pg_default;


CREATE INDEX ON t_top_menu_item
(
	foreign_id
)
WITH
(
	FILLFACTOR = 100
);
