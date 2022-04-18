package SyntaxTreeNode;

import java.util.List;

//语句节点
public class codeNode {
    int line; //所处源代码行数
    int level; //树中层级
    String content; //语句内容 if while ...
    List<Expression> expressions;  //表达式类 + - * / < =

    codeNode(){

    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLine() {
        return line;
    }

    public int getLevel() {
        return level;
    }

    public String getContent() {
        return content;
    }
}
