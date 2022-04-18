package AnalysisTableItem;

import java.util.List;

//基础类型
public class BaseType {
    int size; //所占空间大小
    BaseType kind; //类型 char||integer||array||record
    int low;  //数组下界
    int high; //数组上界 若不是数组类型 则上下界均设为-1
    List<Variable> properties;  //记录类型属性

    BaseType(){

    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public BaseType getKind() {
        return kind;
    }

    public void setKind(BaseType kind) {
        this.kind = kind;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public List<Variable> getProperties() {
        return properties;
    }

    public void setProperties(List<Variable> properties) {
        this.properties = properties;
    }
}
