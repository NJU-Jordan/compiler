import java.util.Map;

public interface Scope {
    public Scope getEnclosingScope();

    public Map<String,Symbol> getSymbols();
    public void define(Symbol symbol);
    public Symbol resolve(String name);

    public void addDerivedScope(Scope derivedScope);
    public Scope nextDerivedScope();
}
