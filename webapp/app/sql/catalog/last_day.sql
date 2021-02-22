CREATE OR REPLACE FUNCTION pg_catalog.last_day
(
	TIMESTAMP WITH TIME ZONE
) RETURNS date AS
$$
declare
begin
    -- 引数の月の最初の日を取得、＋1月して、-1日することにより月末を取得する
	RETURN DATE( ( DATE_TRUNC( 'month', $1 ) + '1 month' ) + '-1 Day' );
end;
$$
LANGUAGE plpgsql
