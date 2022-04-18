package SyntaxTreeNode;

import java.util.List;

//程序体 过程体节点
public class ProBodyNode {
    int level;  //树中层级
    List<codeNode> procodes; //语句列表

    ProBodyNode(){

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
