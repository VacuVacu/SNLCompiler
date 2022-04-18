package SyntaxTreeNode;

import java.util.List;

//类型 变量声明节点
public class Declaration {
    int level;  //所处树中层级
    List<DeclarationNode> declarationNodes; //类型,变量,形参声明列表

    Declaration(){

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
