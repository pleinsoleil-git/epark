DROP TABLE i_clinic CASCADE;


CREATE UNLOGGED TABLE i_clinic
(
	id						BIGSERIAL,
	catalog_id				VARCHAR,
	shopowner_id			VARCHAR,
	latest_opening_time		VARCHAR,
	rich_type				VARCHAR,
	post_code				VARCHAR,
	reservation_type		VARCHAR,
	prov_name				VARCHAR,
	city					VARCHAR,
	district				VARCHAR,
	tel_no					VARCHAR,
	station1				VARCHAR,
	exit_station1			VARCHAR,
	means1					VARCHAR,
	time_required1			VARCHAR,
	station2				VARCHAR,
	means2					VARCHAR,
	exit_station2			VARCHAR,
	time_required2			VARCHAR,
	detail_ss				VARCHAR,
	cv						VARCHAR,
	cvr						VARCHAR,
	cvr_rank				VARCHAR,
	created_at				TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP
)
WITH
(
	FILLFACTOR = 100,
	AUTOVACUUM_ENABLED = FALSE
)
TABLESPACE pg_default;
