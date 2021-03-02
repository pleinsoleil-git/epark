WITH s_params AS
(
SELECT 27::BIGINT AS job_id,
'é°ó√ì‡óe'::VARCHAR AS title
)
SELECT m10.catalog_id,
	t40.title,
	m10.cvr
FROM s_params AS t10
INNER JOIN j_job AS j10
	ON j10.id = t10.job_id
INNER JOIN j_request AS j20
	ON j20.foreign_id = j10.id
	AND j20.deleted = FALSE
INNER JOIN m_clinic AS m10
	ON m10.foreign_id = j10.id
	AND m10.catalog_id = j20.catalog_id
INNER JOIN t_clinic AS t20
	ON t20.foreign_id = j10.id
	AND t20.catalog_id = j20.catalog_id
INNER JOIN t_top_menu AS t30
	ON t30.foreign_id = t20.id
	AND t30.title = t10.title
INNER JOIN t_top_menu_item AS t40
	ON t40.foreign_id = t30.id
WHERE t40.title = 'î¸óeêfó√'
