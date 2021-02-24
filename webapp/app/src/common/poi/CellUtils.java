package common.poi;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;

public class CellUtils {
	public static Row getRow(final Sheet sheet, final int rowIndex) {
		return CellUtil.getRow(rowIndex, sheet);
	}

	public static Cell getCell(final Row row, final int columnIndex) {
		return CellUtil.getCell(row, columnIndex);
	}

	public static void setCellValue(final Cell cell, final Object value) throws Exception {
		if (value != null) {
			if (value instanceof BigDecimal) {
				cell.setCellValue(((BigDecimal) value).doubleValue());
			} else if (value instanceof Long) {
				cell.setCellValue(((Long) value));
			} else if (value instanceof Integer) {
				cell.setCellValue(((Long) value));
			} else if (value instanceof Date) {
				cell.setCellValue(((Date) value));
			} else {
				cell.setCellValue(value.toString());
			}
		}
	}
}
