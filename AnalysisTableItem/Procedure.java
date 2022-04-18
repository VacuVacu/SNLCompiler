package AnalysisTableItem;

import java.util.List;

//过程信息
public class Procedure {
    int level; //表示函数层数 每嵌套一层层数+1
    String name; //过程名
    BaseType kind; //更具返回语句确定的类型
    List<Variable> parameters; //函数参数列表

    Procedure(){

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public List<Variable> getParameters() {
        return parameters;
    }

    public void setParameters(List<Variable> parameters) {
        this.parameters = parameters;
    }
}
