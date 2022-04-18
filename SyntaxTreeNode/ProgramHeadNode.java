package SyntaxTreeNode;

//程序头节点
public class ProgramHeadNode {
    String programName; //程序名
    int line; //所处行数
    int level; //所处树中层级

    ProgramHeadNode(){

    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
