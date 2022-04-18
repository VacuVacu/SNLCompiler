package SyntaxTreeNode;

//类型 变量 形参声明节点
public class DeclarationNode {
    int identity; //通过常量标号区分类型和变量
    int level;    //树中层级
    int line;     //在源代码中所处行数
    int isVar;   //变量则为1 类型为0
    String ID; //标识符名称
    String genre;  //对应的基本类型名

    DeclarationNode(){

    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getIsVar() {
        return isVar;
    }

    public void setIsVar(int isVar) {
        this.isVar = isVar;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
