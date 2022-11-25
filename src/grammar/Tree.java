package grammar;

import file.FileManager;

import java.io.Serializable;
import java.util.Vector;

public class Tree implements Serializable {
    static String filename = "tree.db";
    static Vector<Node> nodes;
    static Vector<Node> roots;
    Vector<Node> nodesTemp = new Vector<>(); // Pour re-créer l'arbre

    public void reCreateTree() throws Exception {
        /* select */
        addNode(null, 0, "SELECT");
        addNode(1, "^[0-9a-zA-Z\\*]+(,[0-9a-zA-Z]+)*$", false, true); // nom de colonne séparé par des virgules et sans espace
        addNode(2, "FROM");
        addNode(3, "[A-Za-z0-9]+", true, true); // nom de table
        addNode(4, "WHERE");
        addNode(5, "[A-Za-z0-9]+[=!<>]+[A-Za-z0-9]+", true, true);

        /* jointure */
        addNode(4, "JOIN");
        addNode(5, "[A-Za-z0-9]+", false, true); // nom de table
        addNode(6, "ON");
        addNode(7, "[A-Za-z0-9]+[=!<>]+[A-Za-z0-9]+", true, true);
        addNode(8, "WHERE");
        addNode(9, "[A-Za-z0-9]+[=!<>]+[A-Za-z0-9]+", true, true);

        /* Difference */
        addNode(0, "DIFFERENCE");
        addNode(1, "[A-Za-z0-9]+", false, true); // nom de table
        addNode(2, "AND");
        addNode(3, "[A-Za-z0-9]+", true, true); // nom de table

        /* Division */
        addNode(0, "DIVIDE");
        addNode(1, "[A-Za-z0-9]+", false, true); // nom de table
        addNode(2, "BY");
        addNode(3, "[A-Za-z0-9]+", true, true); // nom de table

        /* insert */
        addNode(0, "INSERT");
        addNode(1, "INTO");
        addNode(2, "[A-Za-z0-9]+", false, true); // nom de table
        addNode(3, "VALUES");
        addNode(4, "^[0-9a-zA-Z]+(,[0-9a-zA-Z]+)*$", true, true); // valeurs

        /* update */
        addNode(0, "UPDATE");
        addNode(1, "[A-Za-z0-9]+", false, true); // nom de table
        addNode(2, "SET");
        addNode(3, "[A-Za-z0-9]+[=!<>]+[A-Za-z0-9]+", true, true);
        addNode(4, "WHERE");
        addNode(5, "[A-Za-z0-9]+[=!<>]+[A-Za-z0-9]+", true, true);

        /* delete */
        addNode(0, "DELETE");
        addNode(1, "FROM");
        addNode(2, "[A-Za-z0-9]+", true, true); // nom de table
        addNode(3, "WHERE");
        addNode(4, "[A-Za-z0-9]+[=!<>]+[A-Za-z0-9]+", true, true);

        /* table */
        addNode(0, "CREATE");
        addNode(1, "TABLE");
        addNode(2, "[A-Za-z0-9]+", false, true); // nom de table
        addNode(3, "COLS");
        addNode(4, "^[0-9a-zA-Z]+(,[0-9a-zA-Z]+)*$", true, true);


        addNode(0, "DROP");
        addNode(1, "TABLE");
        addNode(2, "[A-Za-z0-9]+", true, true); // nom de table

        addNode(0, "DESC", true);
        addNode(1, "[A-Za-z0-9]+", true, true); // nom de table

        addNode(0, "COMMIT", true);

        addNode(0, "ROLLBACK", true);

        addNode(0, "EXIT", true);

        addNode(0, "[A-Za-z0-9]+", false, true); // nom de table
        addNode(1, "UNION");
        addNode(2, "[A-Za-z0-9]+", true, true); // nom de table

        addNode(1, "INTERSECT");
        addNode(2, "[A-Za-z0-9]+", true, true); // nom de table

        FileManager.clear(getFilename());
        FileManager.save(getFilename(), getNodesTemp());
    }

    /** Operation */
    Node getLastLevel(int level) {
        Node result = null;
        for(Node node:getNodesTemp()) {
            if(node.getLevel() == level)
                result = node;
        }
        return result;
    }

    void addNode(Node parent, int level, String value, boolean isEnd, boolean isPattern) {
        getNodesTemp().add(new Node(parent, level, value, isEnd, isPattern));
    }

    void addNode(Node parent, int level, String value, boolean isEnd) {
        getNodesTemp().add(new Node(parent, level, value, isEnd, false));
    }

    void addNode(Node parent, int level, String value) {
        getNodesTemp().add(new Node(parent, level, value, false, false));
    }

    void addNode(int level, String value, boolean isEnd, boolean isPattern) {
        addNode(getLastLevel(level-1), level, value, isEnd, isPattern);
    }

    void addNode(int level, String value, boolean isEnd) {
        addNode(getLastLevel(level-1), level, value, isEnd, false);
    }

    void addNode(int level, String value) {
        addNode(getLastLevel(level-1), level, value, false, false);
    }

    /** Setter */
    static void setNodes(Vector<Node> nodes) {
        Tree.nodes = nodes;
    }

    void setNodesTemp(Vector<Node> nodesTemp) {
        this.nodesTemp = nodesTemp;
    }

    static void setFilename(String filename) {
        Tree.filename = filename;
    }

    static void setRoots(Vector<Node> roots) {
        Tree.roots = roots;
    }

    /** Getter */
    public static Vector<Node> getNodes() throws Exception {
        if(nodes==null) {
            try {
                setNodes((Vector<Node>) FileManager.load(getFilename()));
            } catch (Exception ignored) { // A changer sur file exception
                Tree tree = new Tree();
                tree.reCreateTree();
                return getNodes();
            }
        }
        return nodes;
    }

    public static Vector<Node> getRoots() throws Exception {
        if(roots == null) {
            setRoots(new Vector<>());
            for (Node node : getNodes())
                if (node.getParent() == null)
                    roots.add(node);
        }
        return roots;
    }

    Vector<Node> getNodesTemp() {
        return nodesTemp;
    }

    static String getFilename() {
        return filename;
    }
}
