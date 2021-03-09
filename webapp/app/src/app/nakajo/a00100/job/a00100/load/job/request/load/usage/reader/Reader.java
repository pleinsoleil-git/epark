package app.nakajo.a00100.job.a00100.load.job.request.load.usage.reader;

import java.io.BufferedReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import app.nakajo.a00100.job.a00100.load.job.request.Request;
import app.nakajo.a00100.job.a00100.load.job.request.load.usage.Record;
import common.io.FileUtils;
import common.lang.StandardCharsets;
import common.lang.StringUtils;
import lombok.val;

public class Reader implements AutoCloseable {
	CSVFormat m_format;
	BufferedReader m_reader;

	public Reader() {
		m_format = CSVFormat.DEFAULT;
	}

	BufferedReader getReader() throws Exception {
		if (m_reader == null) {
			val request = Request.getCurrent();
			val reader = FileUtils.openInputReader(request.getInputFile(), StandardCharsets.MS932);
			m_reader = new BufferedReader(reader);

			// --------------------------------------------------
			// ヘッダーから区切り文字を推測
			// --------------------------------------------------
			val str = m_reader.readLine();
			if (StringUtils.indexOf(str, StringUtils.TAB) >= 0) {
				m_format = m_format.withDelimiter(StringUtils.TAB);
			}
		}

		return m_reader;
	}

	public Record read() throws Exception {
		val str = getReader().readLine();
		if (str != null) {
			return new Record() {
				{
					for (val rec : CSVParser.parse(str, m_format)) {
						int rowNum = 0;
						for (val value : rec) {
							switch (rowNum++) {
							case 0:
								setUsageHistoryId(value);
								break;
							case 1:
								setMediaId(value);
								break;
							case 2:
								setService(value);
								break;
							case 3:
								setUsageDate(value);
								break;
							case 4:
								setMemberId(value);
								break;
							case 5:
								setEvaluation(value);
								break;
							case 6:
								setUsageType(value);
								break;
							case 7:
								setReserve1(value);
								break;
							case 8:
								setReserve2(value);
								break;
							case 9:
								setUsageWithinLast2Year(value);
								break;
							case 10:
								setUsageWithinLast1Year(value);
								break;
							case 11:
								setUsageWithinLast6Month(value);
								break;
							case 12:
								setUsageWithinAfter30Day(value);
								break;
							case 13:
								setUsageWithinAfter60Day(value);
								break;
							case 14:
								setUsageWithinAfter90Day(value);
								break;
							case 15:
								setUsageWithinAfter120Day(value);
								break;
							case 16:
								setUsageWithinAfter150Day(value);
								break;
							case 17:
								setUsageWithinAfter180Day(value);
								break;
							case 18:
								setUsageWithinAfter1Year(value);
								break;
							case 19:
								setUsageWithinAfter2Year(value);
								break;
							case 20:
								setAllUsageWithinLast6Month(value);
								break;
							case 21:
								setAllUsageWithinAfter30Day(value);
								break;
							case 22:
								setAllUsageWithinAfter60Day(value);
								break;
							case 23:
								setAllUsageWithinAfter90Day(value);
								break;
							case 24:
								setAllUsageWithinAfter120Day(value);
								break;
							case 25:
								setAllUsageWithinAfter150Day(value);
								break;
							case 26:
								setAllUsageWithinAfter180Day(value);
								break;
							default:
								break;
							}
						}
					}
				}
			};
		}

		return null;
	}

	@Override
	public void close() throws Exception {
		if (m_reader != null) {
			m_reader.close();
		}
	}
}
