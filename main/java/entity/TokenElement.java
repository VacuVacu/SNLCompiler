package entity;

public class TokenElement {
    private int lineNum = -1;   //行号
    private int itemIdentifier = 0;   //token类型
    private String semanticsInfo = "";  //token内容

    public TokenElement(int lineNum, int itemIdentifier, String semanticsInfo) {
        this.lineNum = lineNum;
        this.itemIdentifier = itemIdentifier;
        this.semanticsInfo = semanticsInfo;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public int getItemIdentifier() {
        return itemIdentifier;
    }

    public void setItemIdentifier(int itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public String getSemanticsInfo() {
        return semanticsInfo;
    }

    public void setSemanticsInfo(String semanticsInfo) {
        this.semanticsInfo = semanticsInfo;
    }
}
