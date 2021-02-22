DROP TABLE t_clinic CASCADE;


CREATE TABLE t_clinic
(
	id						BIGSERIAL,
	foreign_id				BIGINT				REFERENCES j_job( id ) ON DELETE CASCADE,
	catalog_id				VARCHAR( 512 ),
	dental_name				VARCHAR( 512 ),
	total_star				VARCHAR( 512 ),
	review_count			VARCHAR( 512 ),
	net_reserve_type		VARCHAR( 512 ),
	created_at				TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY
	(
		id
	)
	WITH
	(
		FILLFACTOR = 100
	),
	UNIQUE
	(
		foreign_id, catalog_id
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
