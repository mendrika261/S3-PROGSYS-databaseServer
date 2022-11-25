package object;

import exception.query.NotNumberCompException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Vector;

public class Row implements Serializable {
    Vector<String> data;

    /** Constructor */
    public Row(String ...data) {
        setData(data);
    }

    public Row(Vector<String> data) {
        setData(data);
    }

    /** Operator */
    public boolean matchCondition(int col, int comparator, String value) throws Exception {
        boolean match = false;
        switch (comparator) {
            case 0 -> match = !getData().get(col).equals(value);
            case 1 -> match = getData().get(col).equals(value);
            case 2 -> {
                try { match = Double.parseDouble(getData().get(col)) <= Double.parseDouble(value); }
                catch (Exception ignored) { throw new NotNumberCompException("<=", getData().get(col), value); }
            }
            case 3 -> {
                try { match = Double.parseDouble(getData().get(col)) < Double.parseDouble(value); }
                catch (Exception ignored) { throw new NotNumberCompException("<", getData().get(col), value); }
            }
            case 4 -> {
                try { match = Double.parseDouble(getData().get(col)) >= Double.parseDouble(value); }
                catch (Exception ignored) { throw new NotNumberCompException(">=", getData().get(col), value); }
            }
            case 5 -> {
                try { match = Double.parseDouble(getData().get(col)) > Double.parseDouble(value); }
                catch (Exception ignored) { throw new NotNumberCompException(">", getData().get(col), value); }
            }
        }
        return match;
    }

    public boolean equals(Row row) {
        if(getData().size() != row.getData().size()) return false;
        for(int i=0; i<getData().size(); i++)
            if(!getData().get(i).equals(row.getData().get(i))) return false;
        return true;
    }

    public void update(int col, String value) {
        getData().set(col, value);
    }

    public Row join(Row row) {
        Vector<String> data = new Vector<>(getData());
        data.addAll(row.getData());
        return new Row(data);
    }

    @Override
    public String toString() {
        return data.toString()+"("+getData().size()+")";
    }

    /** Setter */
    public void setData(Vector<String> data) {
        this.data = data;
    }

    public void setData(String ...data) {
        setData(new Vector<>(Arrays.asList(data)));
    }

    /** Getter */
    public Vector<String> getData() {
        return data;
    }
}
