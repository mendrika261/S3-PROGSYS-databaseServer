package object;

import exception.object.*;
import interpreter.Operator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Vector;

public class Table implements Serializable {
    String name;
    Vector<String> colNames;
    String textModification;
    Vector<Row> data = new Vector<>();

    /** Constructor */
    public Table(String name, Vector<String> colNames) throws ColDuplicateException {
        setName(name);
        setColNames(colNames);
    }

    public Table(String name, String ...colNames) throws ColDuplicateException {
        this(name, new Vector<>(Arrays.asList(colNames)));
    }

    public Table(String name, Vector<String> colNames, Vector<Row> data) throws ColDuplicateException {
        this(name, colNames);
        setData(data);
    }

    public Table() {

    }

    /** Operation */
    public boolean contains(Row rowToFind) {
        for(Row row:getData()) {
            if (row.equals(rowToFind)) return true;
        }
        return false;
    }

    public void insert(String ...data) throws Exception {
        Row newRow = new Row(data);
        insert(newRow);
    }

    public void insert(Row newRow) throws Exception {
        if(newRow.getData().size()!=getColNames().size())
            throw new ColsNumberNotMatchingException(newRow, this);

        for(Row row:getData()) {
            if (row.equals(newRow)) throw new RowDuplicatedException(newRow, this);
        }
        getData().add(newRow);
    }

    public Table projection(String ...cols) throws Exception {
        if(cols[0].equals("*")) return new Table(getName(), getColNames(), getData());
        Table result = new Table(getName(), cols);
        Row temp;
        for(Row row:getData()) {
            temp = new Row();
            for(String col:cols)
                temp.getData().add(row.getData().get(getColIndex(col)));
            result.getData().add(temp);
        }
        return result;
    }

    public Table selection(String col, int comparator, String value) throws Exception {
        Table result = new Table(getName(), getColNames());
        for(Row row:getData())
            if(row.matchCondition(getColIndex(col),comparator,value))
                result.getData().add(row);
        return result;
    }

    public Table selection(String condition) throws Exception {
        String operator = Operator.getCompOperator(condition);
        String[] conditionPart = condition.split(operator);
        return selection(conditionPart[0], Operator.getCompOperatorCode(condition), conditionPart[1]);
    }

    public void update(String col, String value) throws Exception {
        for(Row row:getData())
            row.update(getColIndex(col), value);
    }

    public void update(String change) throws Exception {
        String[] changePart = change.split("=");
        update(changePart[0], changePart[1]);
    }

    public void delete() {
        getData().clear();
    }

    public Table cartesian(Table table2) throws ColDuplicateException {
        Vector<String> cols = new Vector<>(getColNames());
        cols.addAll(table2.getColNames());
        Table newTable = new Table(this+"-"+table2, cols);
        for(Row row1:getData())
            for(Row row2:table2.getData())
                newTable.getData().add(row1.join(row2));
        return newTable;
    }

    public Table difference(Table table2) throws TableIncompatibleException, ColDuplicateException {
        // if(getColNames().elements() == table2.getColNames().elements())
        //    throw new TableIncompatibleException(getName(), table2.getName());

        Table res = new Table(getName(), getColNames());
        for(Row row:getData())
            if(!table2.contains(row)) {
                try {
                    res.insert(row);
                } catch (Exception ignored) {}
            }
        return res;
    }

    public boolean containsCol(String colToFind) {
        for(String col:getColNames())
            if(col.equals(colToFind)) return true;
        return false;
    }

    public String[] differenceCol(Table table2) {
        Vector<String> res = new Vector<>();

        for(String col:getColNames())
            if(!table2.containsCol(col)) {
                res.add(col);
            }

        return res.toArray(new String[0]);
    }

    public Table division(Table table2) throws Exception {
        Table tab1 = this.projection(this.differenceCol(table2));
        Table tab2 = tab1.cartesian(table2);
        Table tab3 = tab2.difference(this);
        Table tab4 = tab3.projection(this.differenceCol(table2));
        return tab1.difference(tab4);
    }

    public Table join(Table table2, String condition) throws Exception {
        Table product = cartesian(table2);
        Table newTable = new Table(product.getName(), product.getColNames());
        String[] conditionPart = condition.split("==");
        int c1 = product.getColIndex(conditionPart[0]), c2 = product.getColIndex(conditionPart[1]);
        newTable.getColNames().set(c1, getName()+"."+conditionPart[0]);
        newTable.getColNames().set(c2, table2.getName()+"."+conditionPart[0]);
        for(Row row:product.getData())
            if(row.matchCondition(c1, Operator.getCompOperatorCode("=="),  row.getData().get(c2)))
                newTable.getData().add(row);
        return newTable;
    }

    public Table union(Table table2) throws TableIncompatibleException, ColDuplicateException {
        // if(getColNames().elements() == table2.getColNames().elements())
        //    throw new TableIncompatibleException(getName(), table2.getName());

        Table res = new Table(getName() + "-" + table2.getName(), getColNames(), getData());
        for(Row row:table2.getData()) {
            try {
                res.insert(row);
            } catch (Exception ignored) {}
        }
        return res;
    }

    public Table intersects(Table table2) throws TableIncompatibleException, ColDuplicateException {
        // if(getColNames().elements() == table2.getColNames().elements())
        //    throw new TableIncompatibleException(getName(), table2.getName());

        Table res = new Table(getName() + "-" + table2.getName(), getColNames());
        for(Row row:table2.getData()) {
            if(getData().contains(row))
                try {
                    res.insert(row);
                } catch (Exception ignored) {}
        }
        return res;
    }


    public String description() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Table \"").append(getName()).append("\": \n");
        for(String col:getColNames())
            stringBuilder.append("\t").append(col).append("\n");
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return getName()+"("+getColNames().size()+")";
    }

    /** Setter */
    public void setName(String name) {
        this.name = name;
    }

    public void setData(Vector<Row> data) {
        this.data = data;
    }

    public void setColNames(Vector<String> colNames) throws ColDuplicateException {
        /*Vector<String> temp = new Vector<>();
        for(String col:colNames)
            if(temp.contains(col))
                throw new ColDuplicateException(col, getName());
            else
                temp.add(col);*/
        this.colNames = colNames;
    }

    public void setColNames(String ...colNames) throws ColDuplicateException {
        setColNames(new Vector<>(Arrays.asList(colNames)));
    }

    public void setTextModification(String textModification) {
        this.textModification = textModification;
    }

    /** Getter */
    public String getName() {
        return name;
    }

    public Vector<Row> getData() {
        return data;
    }

    public Vector<String> getColNames() {
        return colNames;
    }

    public int getColIndex(String colName) throws Exception {
        int index = getColNames().indexOf(colName);
        if(index == -1)
            throw new ColNotExistsException(colName, this);
        return index;
    }

    public String getTextModification() {
        return textModification;
    }
}
