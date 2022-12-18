package object;

import exception.object.ColMissingException;
import exception.object.TableAlreadyExistsException;
import exception.object.TableNotExistsException;
import exception.query.NameNotAuthorizedException;
import file.FileManager;
import server.Main;

import java.io.Serializable;
import java.util.Vector;

public class Database implements Serializable {
    String name;
    Vector<Table> tables = new Vector<>();

    /** Constructor */
    public Database(String name) throws Exception {
        setName(name);

        loadDatabase();
    }

    /** Operation */
    /* Database */
    public void loadDatabase() throws Exception {
        try {
            Database database = (Database) FileManager.load(getName());
            setTables(database.getTables());
        } catch (Exception ignored) {
            createDatabase();
        }
    }

    void createDatabase() throws Exception {
        FileManager.save(getName(), this);
    }

    void dropDatabase() {
        FileManager.clear(getName());
    }

    public void commit() throws Exception {
        dropDatabase();
        createDatabase();
    }

    public void clearSubQueryTable() throws Exception {
        getTables().removeIf(table -> table.getName().startsWith("subQuery"));
    }
    /* Table */
    public boolean isTable(String name) {
        for(Table table:getTables())
            if(table.getName().equals(name))
                return true;
        return false;
    }

    public void createTable(String name, String ...colNames) throws Exception {
        if(colNames.length==0) throw new ColMissingException();
        if(isTable(name)) throw new TableAlreadyExistsException(getName(), name);
        if(name.equalsIgnoreCase("database")) throw new NameNotAuthorizedException(name);
        getTables().add(new Table(name, colNames));
        commit();
    }

    public void dropTable(String name) throws Exception {
        if(!isTable(name)) throw new TableNotExistsException(getName(), name);
        getTables().remove(getTable(name));
        commit();
    }

    public String description() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Base de donnée \"").append(getName()).append("\": \n");
        for(Table table:getTables())
            stringBuilder.append("\t").append(table.toString()).append("\n");
        return stringBuilder.toString();
    }

    /** Setter */
    public void setName(String name) {
        this.name = name;
    }

    void setTables(Vector<Table> tables) {
        this.tables = tables;
    }

    /** Getter */
    public String getName() {
        return name;
    }

    public Vector<Table> getTables() {
        return tables;
    }

    public Table getTable(String name) throws Exception {
        for(Table table:getTables())
            if(table.getName().equals(name))
                return table;
        throw new TableNotExistsException(getName(), name);
    }
}
