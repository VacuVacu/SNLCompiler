package AnalysisTableItem;

public class ConstValue {
    String name; //名称
    BaseType kind; //类型
    int intVal; //存放值

    ConstValue(){

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

    public int getIntVal() {
        return intVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }
}
