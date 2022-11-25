package interpreter;

import exception.object.TableNotExistsException;
import exception.query.MissingParenthesesException;
import exception.query.SyntaxIncompleteException;
import exception.query.SyntaxInvalidException;
import grammar.Node;
import grammar.Tree;
import object.Database;
import object.Table;

import java.util.Vector;

public class Query {
    Database database;
    String query;
    String[] queryParts;

    /** Constructor */
    public Query(Database database) {
        setDatabase(database);
    }

    /* Operation */
    boolean isSyntaxOK() throws Exception {
        return testSyntax(0, Tree.getRoots());
    }

    boolean testSyntax(int queryIndex, Vector<Node> nodes) throws Exception {
        for(Node node:nodes) {
            // matching correspondance syntaxe et S'IL peut etre la fin
            if(node.match(getQueryPart(queryIndex))) {
                queryIndex++;
                if(getQueryPart(queryIndex) == null) {
                    if (node.isEnd()) return true;
                    else throw new SyntaxIncompleteException(getQuery());
                }
                return testSyntax(queryIndex, node.getChildren());
            }
        }
        throw new SyntaxInvalidException(getQueryPart(queryIndex), getQuery());
    }

    public Table execute() throws Exception {
        Table result = new Table();
        int resultSize=0;
        if(isSyntaxOK())
            switch (getQueryPart(0)) {
                case "SELECT" -> {
                    if (getQueryParts().length == 4) {
                        result = getDatabase().getTable(getQueryPart(3));
                    } else if(getQueryPart(4).equals("JOIN")) {
                        result = getDatabase().getTable(getQueryPart(3)).join(getDatabase().getTable(getQueryPart(5)), getQueryPart(7));
                        if (getQueryParts().length > 8)
                            result = result.selection(getQueryPart(9));
                    //} else if(getQueryPart(1).equals("DIFFERENCE")) {
                    //    result = getDatabase().getTable(getQueryPart(2)).difference(getDatabase().getTable(getQueryPart(4)));
                    } else {
                        result = getDatabase().getTable(getQueryPart(3)).selection(getQueryPart(5));
                    }
                    result = result.projection(getQueryPart(1).split(","));
                }

                case "INSERT" -> {
                    getDatabase().getTable(getQueryPart(2)).insert(getQueryPart(4).split(","));
                    result.setTextModification("Ligne inséré (1)");
                }

                case "UPDATE" -> {
                    result = getDatabase().getTable(getQueryPart(1));
                    if(getQueryParts().length>4)
                        result = result.selection(getQueryPart(5));
                    result.update(getQueryPart(3));
                    resultSize = result.getData().size();
                    result.setTextModification("Ligne modifié ("+resultSize+")");
                }

                case "DELETE" -> {
                    result = getDatabase().getTable(getQueryPart(2));
                    if(getQueryParts().length>4)
                        result = result.selection(getQueryPart(4));
                    resultSize = result.getData().size();
                    result.delete();
                    result.setTextModification("Ligne modifié ("+resultSize+")");
                }

                case "DESC" -> {
                    if(getQueryParts().length==2) {
                        if(!getDatabase().isTable(getQueryPart(1)))
                            throw new TableNotExistsException(getQueryPart(1), getDatabase().getName());
                        result.setTextModification(getDatabase().getTable(getQueryPart(1)).description());
                    } else result.setTextModification(getDatabase().description());
                }

                case "COMMIT" -> {
                    getDatabase().commit();
                    result.setTextModification("Modification enregistré!");
                }

                case "ROLLBACK" -> {
                    getDatabase().loadDatabase();
                    result.setTextModification("Modification annulé!");
                }

                case "CREATE" -> {
                    getDatabase().createTable(getQueryPart(2), getQueryPart(4).split(","));
                    result.setTextModification("Table créé dans la base (1)");
                }

                case "DROP" -> {
                    getDatabase().dropTable(getQueryPart(2));
                    result.setTextModification("Table supprimé dans la base (1)");
                }

                case "DIFFERENCE" -> {
                    result = getDatabase().getTable(getQueryPart(1)).difference(getDatabase().getTable(getQueryPart(3)));
                }

                case "DIVIDE" -> {
                    result = getDatabase().getTable(getQueryPart(1)).division(getDatabase().getTable(getQueryPart(3)));
                }

                default -> {
                    switch (getQueryPart(1)) {
                        case "UNION" -> {
                            result = getDatabase().getTable(getQueryPart(0)).union(getDatabase().getTable(getQueryPart(2)));
                        }
                        case "INTERSECT" -> {
                            result = getDatabase().getTable(getQueryPart(0)).intersects(getDatabase().getTable(getQueryPart(2)));
                        }

                        default -> System.exit(0);
                    }
                }
            } else throw new SyntaxInvalidException(getQuery(), getQuery());
        return result;
    }

    public int getNbSubQuery() throws Exception {
        int open=0;
        int close=0;
        for(int i=0; i<getQuery().length();i++) {
            if(getQuery().charAt(i)=='(') open++;
            else if(getQuery().charAt(i)==')') close++;
        }
        if(open!=close)
            throw new MissingParenthesesException(open, close);
        return open;
    }

    public void executeSubQuery() throws Exception {
        Vector<String> subQuery = new Vector<>();
        StringBuilder temp = new StringBuilder();
        StringBuilder newQuery = new StringBuilder();
        Query subQueryExecutor = new Query(getDatabase());
        boolean inSubQuery=false;
        for(int i=0; i<getQuery().length(); i++) {
            if (getQuery().charAt(i) == ')' && inSubQuery) {
                subQuery.add(temp.toString());
                newQuery.deleteCharAt(newQuery.length()-1);
                subQueryExecutor.setQuery(subQuery.lastElement());
                Table subTable = subQueryExecutor.execute();
                subTable.setName("subQuery"+subQuery.size());
                getDatabase().getTables().add(subTable);
                newQuery.append("subQuery").append(subQuery.size());
                inSubQuery = false;
                temp = new StringBuilder();
                continue;
            }

            if(inSubQuery)
                temp.append(getQuery().charAt(i));
            else
                newQuery.append(getQuery().charAt(i));

            if(getQuery().charAt(i) == '(') {
                if(inSubQuery) {
                    newQuery.append(temp);
                    temp = new StringBuilder();
                }
                inSubQuery = true;
            }
        }
        setQuery(newQuery.toString());
    }

    /** Setter */
    public void setQuery(String query) {
        this.query = query.trim();
        setQueryParts(getQuery().split(" "));
    }

    void setQueryParts(String[] queryParts) {
        this.queryParts = queryParts;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    /** Getter */
    public String getQuery() {
        return query;
    }

    String[] getQueryParts() {
        return queryParts;
    }

    public String getQueryPart(int index) {
        if(index<0 || index>= getQueryParts().length) return null;
        return queryParts[index];
    }

    public Database getDatabase() {
        return database;
    }
}
