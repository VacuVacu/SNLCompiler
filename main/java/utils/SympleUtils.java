package utils;

import SyntaxAnalysis.Derivation;
import SyntaxAnalysis.Symple;
import entity.ConstValues;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SympleUtils {
    public static boolean containsbA(Set<Symple> N_Set, List<Symple> formula, Symple symple){
        return formula.get(formula.size()-1).equals(symple);
    }

    public static boolean containsbAbIsNULL(Set<Symple> N_Set, List<Symple> formula, Symple symple,Map<Symple,Derivation> formulaList){
        if(containsAB(N_Set,formula,symple)){
            Symple aLastSymple = getAlastSymple(formula,symple);
            List<List<Symple>> lst = formulaList.get(aLastSymple).getRight();
            for(List<Symple> form:lst) {
                if(form.contains(new Symple(ConstValues.EMPTY,"$"))) return true;
            }
        }
        return false;
    }

    public static boolean containsAb(Set<Symple> T_Set,List<Symple> formula,Symple symple){
        if(formula.contains(symple)){
            int aindex = formula.indexOf(symple);
            List<Symple> res;
            try {
                res = formula.subList(aindex+1,aindex+2);
            }catch (Exception e){
                return false;
            }
            return T_Set.contains(res.get(0));
        }
        return false;
    }

    public static boolean containsAB(Set<Symple> N_Set, List<Symple> formula, Symple symple){
        if(formula.contains(symple)){
            int aindex = formula.indexOf(symple);
            List<Symple> res;
            try{
                res = formula.subList(aindex+1,aindex+2);
            }catch (Exception e){
                return false;
            }
            return N_Set.contains(res.get(0));
        }
        return false;
    }

    public static  Symple getAlastSymple(List<Symple> formula,Symple symple){
        if(formula.contains(symple)){
            int aindex = formula.indexOf(symple);
            List<Symple> findres = null;
            try{
                findres = formula.subList(aindex+1,aindex+2);
            }catch (Exception e){
                return null;
            }
            return findres.get(0);
        }
        return null;
    }

    public static boolean isEmptyStart(List<Symple> formula){
        return formula.get(0).isEmpty();
    }

    public static boolean isTStart(List<Symple> formula) {
        return formula.get(0).getIdentity()==ConstValues.T_SYMPLE;
    }

    public static boolean isNStart(List<Symple> formula) {
        return formula.get(0).getIdentity()==ConstValues.N_SYMPLE;
    }



}
