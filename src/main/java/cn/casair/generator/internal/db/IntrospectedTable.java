/**
 *    Copyright 2006-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package cn.casair.generator.internal.db;

import java.util.*;


/**
 * Base class for all code generator implementations. This class provides many
 * of the housekeeping methods needed to implement a code generator, with only
 * the actual code generation methods left unimplemented.
 * 
 * @author Jeff Butler
 * 
 */
public class IntrospectedTable {
    /** The fully qualified table. */
    protected FullyQualifiedTable fullyQualifiedTable;

    /** The primary key columns. */
    protected List<IntrospectedColumn> primaryKeyColumns;
    
    /** The base columns. */
    protected List<IntrospectedColumn> baseColumns;
    
    /** The blob columns. */
    protected List<IntrospectedColumn> blobColumns;

    /**
     * Table remarks retrieved from database metadata
     */
    protected String remarks;

    /**
     * Table type retrieved from database metadata
     */
    protected String tableType;

    /**
     * Instantiates a new introspected table.
     */
    public IntrospectedTable() {
        super();
        primaryKeyColumns = new ArrayList<IntrospectedColumn>();
        baseColumns = new ArrayList<IntrospectedColumn>();
        blobColumns = new ArrayList<IntrospectedColumn>();
    }

    public FullyQualifiedTable getFullyQualifiedTable() {
        return fullyQualifiedTable;
    }

    /**
     * Gets the column.
     *
     * @param columnName
     *            the column name
     * @return the column
     */
    public IntrospectedColumn getColumn(String columnName) {
        if (columnName == null) {
            return null;
        } else {
            // search primary key columns
            for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(
                            columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName()
                            .equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }

            // search base columns
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(
                            columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName()
                            .equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }

            // search blob columns
            for (IntrospectedColumn introspectedColumn : blobColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(
                            columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName()
                            .equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }

            return null;
        }
    }

    /**
     * Returns true if any of the columns in the table are JDBC Dates (as
     * opposed to timestamps).
     * 
     * @return true if the table contains DATE columns
     */
    public boolean hasJDBCDateColumns() {
        boolean rc = false;

        for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
            if (introspectedColumn.isJDBCDateColumn()) {
                rc = true;
                break;
            }
        }

        if (!rc) {
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isJDBCDateColumn()) {
                    rc = true;
                    break;
                }
            }
        }

        return rc;
    }

    /**
     * Returns true if any of the columns in the table are JDBC Times (as
     * opposed to timestamps).
     * 
     * @return true if the table contains TIME columns
     */
    public boolean hasJDBCTimeColumns() {
        boolean rc = false;

        for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
            if (introspectedColumn.isJDBCTimeColumn()) {
                rc = true;
                break;
            }
        }

        if (!rc) {
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isJDBCTimeColumn()) {
                    rc = true;
                    break;
                }
            }
        }

        return rc;
    }

    /**
     * Returns the columns in the primary key. If the generatePrimaryKeyClass()
     * method returns false, then these columns will be iterated as the
     * parameters of the selectByPrimaryKay and deleteByPrimaryKey methods
     * 
     * @return a List of ColumnDefinition objects for columns in the primary key
     */
    public List<IntrospectedColumn> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    /**
     * Checks for primary key columns.
     *
     * @return true, if successful
     */
    public boolean hasPrimaryKeyColumns() {
        return primaryKeyColumns.size() > 0;
    }

    /**
     * Gets the base columns.
     *
     * @return the base columns
     */
    public List<IntrospectedColumn> getBaseColumns() {
        return baseColumns;
    }

    /**
     * Returns all columns in the table (for use by the select by primary key and select by example with BLOBs methods).
     *
     * @return a List of ColumnDefinition objects for all columns in the table
     */
    public List<IntrospectedColumn> getAllColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(primaryKeyColumns);
        answer.addAll(baseColumns);
        answer.addAll(blobColumns);

        return answer;
    }

    /**
     * Returns all columns except BLOBs (for use by the select by example without BLOBs method).
     *
     * @return a List of ColumnDefinition objects for columns in the table that are non BLOBs
     */
    public List<IntrospectedColumn> getNonBLOBColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(primaryKeyColumns);
        answer.addAll(baseColumns);

        return answer;
    }

    /**
     * Gets the non blob column count.
     *
     * @return the non blob column count
     */
    public int getNonBLOBColumnCount() {
        return primaryKeyColumns.size() + baseColumns.size();
    }

    /**
     * Gets the non primary key columns.
     *
     * @return the non primary key columns
     */
    public List<IntrospectedColumn> getNonPrimaryKeyColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(baseColumns);
        answer.addAll(blobColumns);

        return answer;
    }

    /**
     * Gets the BLOB columns.
     *
     * @return the BLOB columns
     */
    public List<IntrospectedColumn> getBLOBColumns() {
        return blobColumns;
    }

    /**
     * Checks for blob columns.
     *
     * @return true, if successful
     */
    public boolean hasBLOBColumns() {
        return blobColumns.size() > 0;
    }

    /**
     * Checks for base columns.
     *
     * @return true, if successful
     */
    public boolean hasBaseColumns() {
        return baseColumns.size() > 0;
    }


    /**
     * Checks for any columns.
     *
     * @return true, if successful
     */
    public boolean hasAnyColumns() {
        return primaryKeyColumns.size() > 0 || baseColumns.size() > 0
                || blobColumns.size() > 0;
    }

    /**
     * Sets the fully qualified table.
     *
     * @param fullyQualifiedTable
     *            the new fully qualified table
     */
    public void setFullyQualifiedTable(FullyQualifiedTable fullyQualifiedTable) {
        this.fullyQualifiedTable = fullyQualifiedTable;
    }

    /**
     * Adds the column.
     *
     * @param introspectedColumn
     *            the introspected column
     */
    public void addColumn(IntrospectedColumn introspectedColumn) {
        if (introspectedColumn.isBLOBColumn()) {
            blobColumns.add(introspectedColumn);
        } else {
            baseColumns.add(introspectedColumn);
        }

        introspectedColumn.setIntrospectedTable(this);
    }

    /**
     * Adds the primary key column.
     *
     * @param columnName
     *            the column name
     */
    public void addPrimaryKeyColumn(String columnName) {
        boolean found = false;
        // first search base columns
        Iterator<IntrospectedColumn> iter = baseColumns.iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            if (introspectedColumn.getActualColumnName().equals(columnName)) {
                primaryKeyColumns.add(introspectedColumn);
                iter.remove();
                found = true;
                break;
            }
        }

        // search blob columns in the weird event that a blob is the primary key
        if (!found) {
            iter = blobColumns.iterator();
            while (iter.hasNext()) {
                IntrospectedColumn introspectedColumn = iter.next();
                if (introspectedColumn.getActualColumnName().equals(columnName)) {
                    primaryKeyColumns.add(introspectedColumn);
                    iter.remove();
                    found = true;
                    break;
                }
            }
        }
    }

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
}
