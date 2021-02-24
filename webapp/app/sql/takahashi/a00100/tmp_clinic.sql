DROP TABLE tmp_clinic CASCADE;


CREATE TEMP TABLE tmp_clinic
(
	catalog_id				VARCHAR( 512 ),
	menu_olded				BOOLEAN,
	UNIQUE
	(
		catalog_id
	)
)
