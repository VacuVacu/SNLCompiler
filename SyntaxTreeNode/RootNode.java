package SyntaxTreeNode;

import java.util.List;

//根节点
public class RootNode {
    int level; //所处树中层级
    ProgramHeadNode programHead;
    List<Declaration> declarations;
    ProedureDecNode proedureDec;
    ProBodyNode programBody;

    RootNode(){

    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setProgramHead(ProgramHeadNode programHead) {
        this.programHead = programHead;
    }

    public void setDeclarations(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    public void setProedureDec(ProedureDecNode proedureDec) {
        this.proedureDec = proedureDec;
    }

    public void setProgramBody(ProBodyNode programBody) {
        this.programBody = programBody;
    }

    public int getLevel() {
        return level;
    }

    public ProgramHeadNode getProgramHead() {
        return programHead;
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

    public ProedureDecNode getProedureDec() {
        return proedureDec;
    }

    public ProBodyNode getProgramBody() {
        return programBody;
    }
}
