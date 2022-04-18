package AnalysisTableItem;

//用户声明类型
public class Genre {
    String name ; //标识名称
    BaseType kind; //对应基础类型

    Genre() {

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
}
