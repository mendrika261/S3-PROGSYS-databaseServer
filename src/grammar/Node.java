package grammar;

import java.io.Serializable;
import java.util.Vector;
import java.util.regex.Pattern;

public class Node implements Serializable {
    Node parent;
    String name;
    String value;
    boolean pattern = false; // Utilisation regex pour le nom des colonnes etc...
    boolean end = false; // Vrai si la requete peut s'arreter ici
    int level;
    Vector<Node> children = new Vector<>();

    /** Constructor */
    public Node(Node parent, int level, String value, boolean isEnd) {
        setParent(parent);
        setLevel(level);
        setValue(value);
        setEnd(isEnd);
    }

    public Node(Node parent, int level, String value, boolean isEnd, boolean isPattern) {
        this(parent, level, value, isEnd);
        setPattern(isPattern);
    }

    /** Operation */
    public boolean match(String query) {
        if(isPattern()) return Pattern.matches(getValue(), query);
        else return getValue().equalsIgnoreCase(query);
    }

    /** Setter */
    public void setChildren(Vector<Node> children) {
        this.children = children;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setPattern(boolean pattern) {
        this.pattern = pattern;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Getter */
    public Node getParent() {
        return parent;
    }

    public int getLevel() {
        return level;
    }

    public String getValue() {
        return value;
    }

    public Vector<Node> getChildren() throws Exception {
        for(Node node:Tree.getNodes()) {
            if (node.getParent() != null && equals(node.getParent()))
                children.add(node);
        }
        return children;
    }

    public boolean isPattern() {
        return pattern;
    }

    public boolean isEnd() {
        return end;
    }

    public String getName() {
        if(name==null) return getValue();
        return name;
    }
}
