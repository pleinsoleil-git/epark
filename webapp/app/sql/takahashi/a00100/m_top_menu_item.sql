DROP TABLE m_top_menu_item CASCADE;


CREATE TABLE m_top_menu_item
(
	id						BIGSERIAL,
	menu					VARCHAR( 1024 ),
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
	menu, title
)
WITH
(
	FILLFACTOR = 100
);


INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '歯が痛い', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '2回目以降の方', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '歯ぐきが腫れた', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', 'クリーニング', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '詰め物が取れた', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', 'かぶせ物がとれた', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '虫歯・歯周病の予防', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '噛めない', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '歯がしみる', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '親知らずの抜歯', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '口があかない', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '顎が痛い', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '歯を白くしたい', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '見た目をキレイにしたい', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '歯並びの相談', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '入れ歯が合わない', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '入れ歯が壊れた', TRUE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '虫歯', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '顎関節症', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', 'クリーニング', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', 'インプラント', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', 'ホワイトニング', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', 'ラミネートベニア', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '歯科検診', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '一般矯正', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '噛み合わせ', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '歯周病', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '訪問歯科診療', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', 'つめ物・かぶせ物', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '口臭', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '親知らず', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '入れ歯・義歯', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '小児歯科', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '小児矯正', FALSE );
INSERT INTO m_top_menu_item( menu, title, olded ) VALUES ( '治療内容', '院内予約', FALSE );
