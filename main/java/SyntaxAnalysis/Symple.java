package SyntaxAnalysis;

import java.util.Objects;

public class Symple implements Comparable<Symple>{
    boolean empty = false;  //是否为空生成式
    boolean start = false;  //是否为开始符
    int identity;  //符号类型 用于区分终极符和非终极符和空
    String content;  //文法内容

    public boolean isEmpty() {
        return empty;
    }

    public int getIdentity() {
        return identity;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Symple{" +
                "empty=" + empty +
                ", identity=" + identity +
                ", content='" + content + '\'' +
                '}';
    }

    public Symple(int identity, String content) {
        this.start = false;
        this.identity = identity;
        this.content = content;
        this.empty = false;
        if(Objects.equals(content, "#")) this.empty = true;
    }

    @Override
    public int compareTo(Symple o) {
        return this.content.compareTo(o.content);
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
        Symple symple = (Symple) o;
        return identity == symple.identity && content.equals(symple.content);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(content);
//    }
}
