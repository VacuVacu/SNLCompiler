package SyntaxTreeNode;

//表达式节点 用于语句体中
public class Expression {
    int isConst;  //常量为1 变量为0
    int value; //如果是常量 存储常量值 否则为0
    String ID;  //变量名 常量为空
    String Op;  //操作类型 + - * / = < 若为结尾 则为空
    Expression exp; //后续因子

    Expression(){

    }

    public int getIsConst() {
        return isConst;
    }

    public void setIsConst(int isConst) {
        this.isConst = isConst;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOp() {
        return Op;
    }

    public void setOp(String op) {
        Op = op;
    }

    public Expression getExp() {
        return exp;
    }

    public void setExp(Expression exp) {
        this.exp = exp;
    }
}
