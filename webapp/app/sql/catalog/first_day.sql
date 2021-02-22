CREATE OR REPLACE FUNCTION pg_catalog.first_day
(
	TIMESTAMP WITH TIME ZONE
) RETURNS date AS
$$
declare
begin
	RETURN DATE( DATE_TRUNC( 'month', $1 ) );
end;
$$
LANGUAGE plpgsql
