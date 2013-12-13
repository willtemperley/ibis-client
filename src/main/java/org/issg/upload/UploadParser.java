package org.issg.upload;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.NonUniqueResultException;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jrc.persist.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author will
 * 
 *         Operates on Excel {@link Row}s, transferring information to entities.
 *         All errors are recorded and can be retrieved after processing.
 *         Subclasses should override processRow which returns an entity for
 *         each row. If this method fails, ideally it should do so silently and
 *         record the error to allow the entire work sheet to be processed.
 * 
 * @param <E>
 *            The entity type being uploaded.
 */
public abstract class UploadParser<E> {

    private List<String> errors = new ArrayList<String>();

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected enum CellType {
        NUMERIC, STRING, FORMULA, BLANK, BOOLEAN, ERROR;
    }

    private boolean allowSkippedRows = false;

    protected final Validator validator;

    protected List<E> entityList = new ArrayList<E>();

    protected List<String> colHeaders = new ArrayList<String>();

    protected Dao dao;

    public UploadParser(Dao dao, Class<E> clazz) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.dao = dao;

    }

    public void allowSkippedRows(boolean allowSkippedRows) {
        this.allowSkippedRows = allowSkippedRows;
    }

    /**
     * Iterates all data rows in a spreadsheet, ignoring header rows and
     * stopping as soon as a row with no data is found. Calls a subclasses
     * provided method to process a row which returns an extracted entity.
     * 
     * @param sheet
     * @param headerRowIdx
     */
    public void processSheet(Sheet sheet, int headerRowIdx) {

        Row headerRow = sheet.getRow(headerRowIdx);
        for (Cell cell : headerRow) {
            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty())
                System.out.println(cell.getStringCellValue());
        }
        populateColumnHeaders(headerRow);

        errors.clear();
        entityList.clear();

        for (Row row : sheet) {
            

            int rowNum = row.getRowNum();

            if (rowNum > headerRowIdx && rowHasData(row)) {
                E entity = processRow(row);

                /*
                 * When null entities are found this may be because we wish to
                 * skip certain rows (e.g. those which may be processed later).
                 */
                if (entity == null && allowSkippedRows) {
//                    logger.info("Skipped row: " + rowNum);
                    continue;
                }
                validateEntity(entity, row);
                entityList.add(entity);
            }
        }
    }

    /**
     * Returns true if the row has at least one cell with data.
     * 
     * @param row
     * @return
     */
    private boolean rowHasData(Row row) {
        
        Cell cell = row.getCell(0);
        return cellHasData(cell);

//        for (int i = 0; i < colHeaders.size(); i++) {
//            cell = row.getCell(i);
//            if (cellHasData(cell)) {
//                return true;
//            }
//        }
        
//        return false;
    }

    private boolean cellHasData(Cell cell) {
        if (cell == null) {
            return false;
        } else {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String val = cell.getStringCellValue();
            if (val == null || val.isEmpty()) {
                return false;
            }
            return true;
        }
    }

    protected abstract E processRow(Row row);

    protected abstract void processWorkbook(Workbook workbook);

    @SuppressWarnings("unchecked")
    @Deprecated
    protected <T> T getCellValue(Cell cell, Class<T> clazz) {

        if (cell == null) {
            return null;
        }

        int cellIdx = cell.getRowIndex();

        int rowNum = cell.getRowIndex();
        int cellType = cell.getCellType();

        if (cellType == Cell.CELL_TYPE_NUMERIC) {

            Double val = cell.getNumericCellValue();

            if (clazz.equals(Long.class)) {
                return (T) new Long(val.longValue());
            } else if (clazz.equals(Integer.class)) {
                double diff = val - Math.round(val);
                diff = Math.sqrt(Math.pow(diff, 2));
                if (diff > 0.0000001) {
                    recordError(rowNum, cellIdx, val
                            + " is not a valid integer.");
                    return null;
                }
                return (T) new Integer(val.intValue());
            } else if (clazz.equals(Double.class)) {
                return (T) val;
            } else if (clazz.equals(BigDecimal.class)) {
                return (T) BigDecimal.valueOf(val);
            }
            recordError(rowNum, cellIdx, "Expected " + clazz
                    + " got numeric value");

        } else if (cellType == Cell.CELL_TYPE_STRING) {
            if (clazz.equals(String.class)) {
                return (T) cell.getStringCellValue();
            }
            recordError(rowNum, cellIdx, "Expected " + clazz + " got String");
        } else if (cellType == Cell.CELL_TYPE_BOOLEAN) {
            if (clazz.equals(Boolean.class)) {
                return (T) (Boolean) cell.getBooleanCellValue();
            }
            recordError(rowNum, cellIdx, "Expected " + clazz + " got Boolean");
        } else if (cellType == Cell.CELL_TYPE_BLANK) {
            return null;
        } else {
            recordError(rowNum, cellIdx,
                    "Invalid cell type: " + CellType.values()[cellType]);
        }

        return null;
    }

    protected <T> T getCellValue(Row row, int colIdx, Class<T> clazz) {

        Cell cell = row.getCell(colIdx);
        return getCellValue(cell, clazz);
    }

    /**
     * Gets a cell value as a string literal, regardless of the actual type.
     * 
     * @param row
     * @param colIdx
     * @return
     */
    protected String getCellValueAsString(Row row, int colIdx) {
        Cell cell = row.getCell(colIdx);
        if (cell == null) {
            return null;
        }
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String val = cell.getStringCellValue();
        return val;
    }

    protected Long getCellValueAsLong(Row row, int colIdx) {
        String cellValue = getCellValueAsString(row, colIdx);
        if (cellValue == null || cellValue.isEmpty()) {
            return null;
        } else {
            try {
                return Long.valueOf(cellValue);
            } catch (NumberFormatException e) {
                recordError(row.getRowNum(), colIdx,
                        "Expected integer, got value: " + cellValue);
            }
        }
        return null;
    }

    protected void validateEntity(E entity, Row row) {
        Set<ConstraintViolation<E>> constraintViolations = validator
                .validate(entity);
        recordConstraintViolations("Row " + (row.getRowNum() + 1),
                constraintViolations);
    }

    /**
     * 
     * @param <T>
     *            the class the constraint violations pertain to
     * @param ref
     *            the sheet row reference
     * @param constraintViolations
     *            a set of violations
     */
    protected <T> void recordConstraintViolations(String ref,
            Set<ConstraintViolation<T>> constraintViolations) {

        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            recordError(ref + ", Property "
                    + constraintViolation.getPropertyPath() + " "
                    + constraintViolation.getMessage());
        }
    }

    /**
     * Records an error, giving the cell address and a meaningful message.
     * 
     * @param cellIdx
     *            TODO
     * @param message
     * @param col
     */
    protected void recordError(int rowNum, int cellIdx, String message) {
        String colName = colHeaders.get(cellIdx);
        StringBuffer sb = new StringBuffer();
        sb.append("Row ");
        sb.append(rowNum + 1);
        sb.append(", Column \"");
        sb.append(colName);
        sb.append("\" ");
        sb.append(message);
        errors.add(sb.toString());
    }

    protected void recordError(String string) {
        errors.add(string);
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<E> getEntityList() {
        return entityList;
    }

    protected void populateColumnHeaders(Row firstRow) {
        for (Cell cell : firstRow) {
            colHeaders.add(cell.getStringCellValue());
        }
    }

    /**
     * Look up an entity from the {@link StaticMetamodel} attribute given and
     * row / column reference
     * 
     * @param attr
     *            the {@link StaticMetamodel} {@link Attribute}
     * @param row
     *            the {@link Row} from the {@link Workbook}
     * @param colIdx
     *            the index of the column in the row
     * @return the entity if found, otherwise null
     * 
     */
    protected <T> T getEntity(SingularAttribute<T, String> attr, Row row,
            int colIdx) {

        String lookUp = this.getCellValueAsString(row, colIdx);

        if (lookUp == null || lookUp.isEmpty()) {
            return null;
        }

        String trimmedLookUp = lookUp.replace(String.valueOf((char) 160), " ");
        trimmedLookUp = trimmedLookUp.trim();
        
//        if (lookUp.startsWith("Aca"))
//        System.out.println("===" + lookUp + "====");

        return getEntity(attr, row, colIdx, trimmedLookUp);
    }

    /**
     * Look up an entity from the {@link StaticMetamodel} attribute given and a
     * proxy lookup
     * 
     * @param attr
     *            the {@link StaticMetamodel} {@link Attribute}
     * @param row
     *            the {@link Row} from the {@link Workbook}
     * @param colIdx
     *            the index of the column in the row
     * @return the entity if found, otherwise null
     * 
     */
    protected <T> T getEntity(SingularAttribute<T, String> attr, Row row,
            int colIdx, String lookUp) {

        // Get entity simple name
        String entityClassName = attr.getDeclaringType().getJavaType()
                .getSimpleName();

        try {
            T e = dao.findByProxyId(attr, lookUp);
            if (e == null) {
                recordError(row.getRowNum(), colIdx, String.format(
                        "Could not find %s with name \"%s\"", entityClassName,
                        lookUp));
            }
            return e;
        } catch (NonUniqueResultException e) {
            recordError(row.getRowNum(), colIdx, String.format(
                    "Multiple entries for %s with lookup %s found.",
                    entityClassName, lookUp));
        }
        return null;
    }

}
