DROP TABLE m_top_menu_item CASCADE;


CREATE TABLE m_top_menu_item
(
	id						BIGSERIAL,
	title					VARCHAR( 1024 ),
	olded					BOOLEAN,
	deleted					BOOLEAN			DEFAULT FALSE,
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


CREATE INDEX ON m_top_menu_item
(
	title
)
WITH
(
	FILLFACTOR = 100
);


INSERT INTO m_top_menu_item( title, olded ) VALUES ('歯が痛い', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('2回目以降の方', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('歯ぐきが腫れた', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('クリーニング', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('詰め物が取れた', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('かぶせ物がとれた', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('虫歯・歯周病の予防', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('噛めない', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('歯がしみる', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('親知らずの抜歯', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('口があかない', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('顎が痛い', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('歯を白くしたい', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('見た目をキレイにしたい', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('歯並びの相談', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('入れ歯が合わない', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('入れ歯が壊れた', TRUE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('虫歯', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('顎関節症', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('クリーニング', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('インプラント', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('ホワイトニング', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('ラミネートベニア', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('歯科検診', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('一般矯正', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('噛み合わせ', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('歯周病', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('訪問歯科診療', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('つめ物・かぶせ物', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('口臭', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('親知らず', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('入れ歯・義歯', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('小児歯科', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('小児矯正', FALSE );
INSERT INTO m_top_menu_item( title, olded ) VALUES ('院内予約', FALSE );
