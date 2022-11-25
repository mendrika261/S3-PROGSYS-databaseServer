package display;

import object.Row;
import object.Table;

public class Console {
    public static String print(Table table) {
        StringBuilder stringBuilder = new StringBuilder();
        if(table.getTextModification()!=null) {
            stringBuilder.append(table.getTextModification());
            return stringBuilder.toString();
        }
        stringBuilder.append("\n");
        for (String col:table.getColNames())
            stringBuilder.append("[").append(col).append("]\t\t");
        stringBuilder.append("\n\n");
        for (Row row: table.getData())
            stringBuilder.append(Console.print(row));
        stringBuilder.append("\n");
        stringBuilder.append("(Nombre de ligne retourné: ").append(table.getData().size()).append(")");
        return stringBuilder.toString();
    }

    public static String print(Row row) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String col: row.getData())
            stringBuilder.append(col).append("\t");
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
