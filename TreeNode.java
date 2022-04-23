package node;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    //孩子节点 若nodeKind=ProK 0指向程序头 1指向类型声明 2指向变量声明 3指向过程声明 4指向程序体
    //若nodeKind=ProcDecK 0无意义 参数包含于attr属性 1指向类型声明 2指向变量声明 3指向过程声明 4指向过程体
    public TreeNode[] children;
    public TreeNode sibling;   //兄弟节点
    public int line; //源代码行数
    //节点类型可为 ProK(程序) PheadK(程序头) TypeK(类型) VarK(变量) ProcDecK(过程声明) StmLK DecK(声明) StmtK(语句) ExpK(表达式）
    //ParamK(参数)
    public int nodeKind; //为0无意义
//    public int isBro;  //是否是某个节点的兄弟节点
//    public int isfirst; // 是否是第一个声明节点

    //nodeKind=DecK时为 ArrayK,CharK,IntegerK,RecordK,IdK 之一
    //nodeKind=StmtK时为 ifK,WhileK,AssignK,ReadK,WriteK,CallK,ReturnK 之一
    //nodeKind=ExpK时为 OpK,ConstK,IdK,ParamK 之一
    public int kind; //为0无意义
    public List<String> name;  //节点中标识符名字
    public String type_name;  //节点为声明 且类型由类型标识符表示有效

    public Attr attr;  //额外属性 对于数组，过程，Exp

    public TreeNode(){
//        isBro = 0;
//        isfirst = 0;
        nodeKind = 0;
        kind = 0;
        this.children = new TreeNode[5];
        this.name = new ArrayList<>();
    }

}
