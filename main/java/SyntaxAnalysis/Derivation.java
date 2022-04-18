package SyntaxAnalysis;

import java.util.ArrayList;
import java.util.List;

public class Derivation {
    Symple left; //生成式左部
    List<List<Symple>> right;  //生成式右部

    public Derivation() {
    }

    public Derivation(ReturnValue p) {
        this.left = new Symple(p.symples.get(0).getIdentity(), p.symples.get(0).content);
        this.right = new ArrayList<>();
        int start = 1;
        int index = 0;
        for (int i = 0; i < p.index.size(); i++) {
            index = p.index.get(i);
            List<Symple> res = new ArrayList<>(p.symples.subList(start, index));
            start = index;
            this.right.add(res);
        }
        List<Symple> res = new ArrayList<>(p.symples.subList(start, p.symples.size()));
        this.right.add(res);
    }

    public Symple getLeft() {
        return left;
    }

    public List<List<Symple>> getRight() {
        return right;
    }

}
