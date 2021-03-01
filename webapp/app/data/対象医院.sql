TRUNCATE TABLE j_request CASCADE;


COPY j_request
(
	catalog_id
)
FROM 'D:/Temp/clinic.csv'


UPDATE j_request
SET foreign_id = 27;
