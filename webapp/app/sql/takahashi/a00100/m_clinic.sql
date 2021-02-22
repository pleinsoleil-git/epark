DROP TABLE m_clinic CASCADE;


CREATE TABLE m_clinic
(
	id						BIGSERIAL,
	foreign_id				BIGINT				REFERENCES j_job( id ) ON DELETE CASCADE,
	catalog_id				VARCHAR( 512 ),
	shopowner_id			VARCHAR( 512 ),
	latest_opening_time		VARCHAR( 512 ),
	rich_type				VARCHAR( 512 ),
	post_code				VARCHAR( 512 ),
	reservation_type		VARCHAR( 512 ),
	prov_name				VARCHAR( 512 ),
	city					VARCHAR( 512 ),
	district				VARCHAR( 512 ),
	tel_no					VARCHAR( 512 ),
	station1				VARCHAR( 512 ),
	exit_station1			VARCHAR( 512 ),
	means1					VARCHAR( 512 ),
	time_required1			VARCHAR( 512 ),
	station2				VARCHAR( 512 ),
	means2					VARCHAR( 512 ),
	exit_station2			VARCHAR( 512 ),
	time_required2			VARCHAR( 512 ),
	detail_ss				NUMERIC,
	cv						NUMERIC,
	cvr						NUMERIC,
	cvr_rank				NUMERIC,
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
