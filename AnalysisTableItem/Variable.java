package AnalysisTableItem;

//变量信息 可重用为记录属性 过程参数
public class Variable {
    String name; //变量名
    BaseType kind; //变量类型
    int access; //直接变量为1 间接变量为0
    int level; //变量层数 用作结构体属性时为 -1 用作过程参数为-2
    int off;   //主程序或过程偏移

    Variable(){
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseType getKind() {
        return kind;
    }

    public void setKind(BaseType kind) {
        this.kind = kind;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getOff() {
        return off;
    }

    public void setOff(int off) {
        this.off = off;
    }
}
