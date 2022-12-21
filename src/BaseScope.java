import java.util.LinkedHashMap;
import java.util.Map;

public class BaseScope implements Scope {
    private final Scope enclosingScope;
    private final Map<String, Symbol> symbols = new LinkedHashMap<>(); //符号表
    private String name;

    public BaseScope(String name, Scope enclosingScope) {
        this.name = name;
        this.enclosingScope = enclosingScope;
    }

    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }
    //
    public void define(Symbol symbol){

        symbols.put(symbol.getName(),symbol);
        System.out.println("+" + symbol.getName());
    }
    //解析符号
    public Symbol resolve(String name){
        Symbol symbol =symbols.get(name);
        if(symbol!=null) {
            System.out.println("*" + name);
            return symbol;
        }
        if(enclosingScope!=null) {
            //如果父作用域不为空，在父作用域下解析符号
            return enclosingScope.resolve(name);
        }
        System.err.println("Cannot find" +name);
        return null;

    }
}
