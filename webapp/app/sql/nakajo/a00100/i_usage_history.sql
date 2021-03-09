DROP TABLE i_usage_history CASCADE;


CREATE UNLOGGED TABLE i_usage_history
(
	id								BIGSERIAL,
	usage_history_id				VARCHAR,	-- id
	media_id						VARCHAR,	-- media_id
	service							VARCHAR,	-- サービス
	usage_date						VARCHAR,	-- 日付
	member_id						VARCHAR,	-- EPARK会員ID
	evaluation						VARCHAR,	-- 評価
	channel							VARCHAR,	-- 種別
	reserve_1						VARCHAR,	-- 予備1
	reserve_2						VARCHAR,	-- 予備2
	usage_within_last_2_year		VARCHAR,	-- 過去2年以内利用回数
	usage_within_last_1_year		VARCHAR,	-- 過去1年以内利用回数
	usage_within_last_6_month		VARCHAR,	-- 過去6ヵ月利用回数
	usage_within_after_30_day		VARCHAR,	-- その後30日以内利用回数
	usage_within_after_60_day		VARCHAR,	-- その後60日以内利用回数
	usage_within_after_90_day		VARCHAR,	-- その後90日以内利用回数
	usage_within_after_120_day		VARCHAR,	-- その後120日以内利用回数
	usage_within_after_150_day		VARCHAR,	-- その後150日以内利用回数
	usage_within_after_180_day		VARCHAR,	-- その後180日以内利用回数
	usage_within_after_1_year		VARCHAR,	-- その後1年以内利用
	usage_within_after_2_year		VARCHAR,	-- その後2年以内利用
	all_usage_within_last_6_month	VARCHAR,	-- （ALL）過去6ヵ月利用
	all_usage_within_after_30_day	VARCHAR,	-- （ALL）その後30日以内利用
	all_usage_within_after_60_day	VARCHAR,	-- （ALL）その後60日以内利用
	all_usage_within_after_90_day	VARCHAR,	-- （ALL）その後90日以内利用
	all_usage_within_after_120_day	VARCHAR,	-- （ALL）その後120日以内利用
	all_usage_within_after_150_day	VARCHAR,	-- （ALL）その後150日以内利用
	all_usage_within_after_180_day	VARCHAR,	-- （ALL）その後180日以内利用
	created_at						TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP
)
WITH
(
	FILLFACTOR = 100,
	AUTOVACUUM_ENABLED = FALSE
)
TABLESPACE pg_default;
