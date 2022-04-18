package SyntaxTreeNode;

import java.util.List;

//过程声明节点
public class ProedureDecNode {
    int level; //所处树中层级
    String genre;  //有返回值的为返回值类型 否则为空
    String procedureName; //程序名
    List<DeclarationNode> paramters; //形参列表
    List<Declaration> declarations; //类型和变量声明
    ProedureDecNode proedure; //嵌套过程声明
    ProBodyNode procedureBody; //过程体

    ProedureDecNode(){

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public ProedureDecNode getProedure() {
        return proedure;
    }

    public void setProedure(ProedureDecNode proedure) {
        this.proedure = proedure;
    }

    public ProBodyNode getProcedureBody() {
        return procedureBody;
    }

    public void setProcedureBody(ProBodyNode procedureBody) {
        this.procedureBody = procedureBody;
    }
}
