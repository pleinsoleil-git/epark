package common.poi;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

public class CellUtils {
	public static void setCellValue(final Cell cell, final Object value) throws Exception {
		if (value != null) {
			if (value instanceof BigDecimal) {
				cell.setCellValue(((BigDecimal) value).doubleValue());
			} else if (value instanceof Long) {
				cell.setCellValue(((Long) value));
			} else if (value instanceof Date) {
				cell.setCellValue(((Date) value));
			} else {
				cell.setCellValue(value.toString());
			}
		}
	}
}
