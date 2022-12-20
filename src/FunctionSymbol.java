public class FunctionSymbol extends BaseScope implements Symbol{
    public FunctionSymbol(String name,Type type, Scope enclosingScope) {
        super(name, enclosingScope);
    }
}
